package forum.hub.api.domain.course;

import java.util.Set;

public record CourseDetailDTO(
    Long id,
    String name,
    Set<CourseCategory> categories) {

    public CourseDetailDTO(Course course) {
        this(course.getId(), course.getName(), course.getCategories());
    }
}
