package forum.hub.api.domain.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicCreateDTO(
        @NotBlank
        String title,
        @NotBlank
        String message,
        @NotNull
        Long authorId,
        @NotNull
        Long courseId) {
}
