package forum.hub.api.controller;


import forum.hub.api.domain.topic.TopicCreateDTO;
import forum.hub.api.domain.topic.TopicViewDTO;
import forum.hub.api.domain.topic.TopicService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Year;


@RestController
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicService service;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid TopicCreateDTO data, UriComponentsBuilder uriBuilder) {
        var topic = service.create(data);

        var uri = uriBuilder.path("/topics/{id}").buildAndExpand(topic.getId()).toUri();

        return ResponseEntity.created(uri).body(new TopicViewDTO(topic));
    }

    @GetMapping
    public ResponseEntity list(@PageableDefault(sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable,
                               @RequestParam(required = false) String course,
                               @RequestParam(required = false) Year year) {
        var topics = service.getTopics(course, year, pageable)
                .map(TopicViewDTO::new);

        return ResponseEntity.ok(topics);
    }
}
