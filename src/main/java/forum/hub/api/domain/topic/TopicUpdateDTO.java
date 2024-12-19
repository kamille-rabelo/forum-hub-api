package forum.hub.api.domain.topic;

public record TopicUpdateDTO(
        String title,
        String message,
        Long courseId) {
}
