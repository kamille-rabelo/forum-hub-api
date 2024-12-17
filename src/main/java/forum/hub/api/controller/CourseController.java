package forum.hub.api.controller;

import forum.hub.api.domain.course.CourseCreateDTO;
import forum.hub.api.domain.course.CourseDetailDTO;
import forum.hub.api.domain.course.CourseService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService service;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid CourseCreateDTO data) {
        var course = service.create(data);

        return ResponseEntity.created(null).body(new CourseDetailDTO(course));
    }
}
