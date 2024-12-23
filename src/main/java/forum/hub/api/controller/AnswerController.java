package forum.hub.api.controller;

import forum.hub.api.domain.answer.AnswerPostDTO;
import forum.hub.api.domain.answer.AnswerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answers")
@SecurityRequirement(name = "bearer-key")
public class AnswerController {

    @Autowired
    private AnswerService service;

    @PostMapping
    public ResponseEntity post(@RequestBody @Valid AnswerPostDTO data) {
        return ResponseEntity.ok(service.post(data));
    }

}
