package forum.hub.api.controller;


import forum.hub.api.domain.topic.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearer-key")
public class TopicController {

    @Autowired
    private TopicService service;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid TopicCreateDTO data, UriComponentsBuilder uriBuilder) {
        var topic = service.create(data);

        var uri = uriBuilder.path("/topics/{id}").buildAndExpand(topic.id()).toUri();

        return ResponseEntity.created(uri).body(topic);
    }

    @PostMapping("/{topicId}/answers/{answerId}/mark-solved")
    @Transactional
    public ResponseEntity markSolved(@PathVariable Long topicId, @PathVariable Long answerId) {
        System.out.println("service called");
        service.markSolved(topicId, answerId);

        System.out.println("service called 2");
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity list(@PageableDefault(sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable,
                               @RequestParam(required = false) String course,
                               @RequestParam(required = false) Year year) {

        return ResponseEntity.ok(service.getTopics(course, year, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTopicDetailById(id));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity update(@PathVariable Long id, @RequestBody TopicUpdateDTO data) {
        return ResponseEntity.ok(service.update(id, data));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
