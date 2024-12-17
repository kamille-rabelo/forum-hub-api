package forum.hub.api.domain.course;

import forum.hub.api.domain.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        repository.save(course);
        return course;
    }

    public Page<Course> getCourses(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
