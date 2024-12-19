package forum.hub.api.domain.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnswerPostDTO(
        @NotBlank
        String message,
        @NotNull
        Long authorId,
        @NotNull
        Long topicId) {
}
