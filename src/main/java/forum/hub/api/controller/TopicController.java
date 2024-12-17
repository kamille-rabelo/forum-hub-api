package forum.hub.api.controller;


import forum.hub.api.domain.topic.TopicCreateDTO;
import forum.hub.api.domain.topic.TopicService;
import forum.hub.api.domain.topic.TopicViewDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


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
}
