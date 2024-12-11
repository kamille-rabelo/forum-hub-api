package forum.hub.api.domain.topic;

import java.time.LocalDateTime;

public record TopicViewDTO(
    Long id,
    String title,
    String message,
    LocalDateTime creationDate,
    Boolean solved) {

    public TopicViewDTO(Topic topic) {
        this(topic.getId(), topic.getTitle(),
                topic.getMessage().substring(0, Math.min(topic.getMessage().length(), 100)),
                topic.getCreationDate(), topic.getSolved());
    }
}
