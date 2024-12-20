package forum.hub.api.controller;

import forum.hub.api.domain.course.CourseCreateDTO;
import forum.hub.api.domain.course.CourseDetailDTO;
import forum.hub.api.domain.course.CourseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@SecurityRequirement(name = "bearer-key")
public class CourseController {

    @Autowired
    private CourseService service;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid CourseCreateDTO data) {
        var course = service.create(data);

        return ResponseEntity.created(null).body(new CourseDetailDTO(course));
    }

    @GetMapping
    public ResponseEntity list(@PageableDefault(sort = "name") Pageable pageable) {
        var courses = service.getCourses(pageable)
                .map(CourseDetailDTO::new);

        return ResponseEntity.ok(courses);
    }
}
