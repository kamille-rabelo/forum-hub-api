package forum.hub.api.domain.course;

import forum.hub.api.domain.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseRepository repository;

    public Course create(CourseCreateDTO data) {
        var courseExists = repository.existsByName(data.name());
        if (courseExists) {
            throw new ValidationException("Course already exists");
        }

        var course = new Course(null, data.name(), data.categories());
        System.out.println(course);

        repository.save(course);

        return course;
    }
}
