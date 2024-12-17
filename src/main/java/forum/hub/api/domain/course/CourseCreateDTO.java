package forum.hub.api.domain.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CourseCreateDTO(
        @NotBlank
        String name,
        @NotNull
        Set<CourseCategory> categories) {
}
