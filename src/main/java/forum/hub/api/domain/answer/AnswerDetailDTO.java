package forum.hub.api.domain.answer;

import java.time.LocalDateTime;

public record AnswerDetailDTO(
        Long id,
        String topic,
        String message,
        String author,
        LocalDateTime creationDate,
        Boolean solution) {


    public AnswerDetailDTO(Answer post) {
        this(post.getId(), post.getTopic().getTitle(), post.getMessage(), post.getAuthor().getName(),
                post.getCreationDate(), post.getSolution());
    }
}
