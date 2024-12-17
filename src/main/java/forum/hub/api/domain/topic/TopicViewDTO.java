package forum.hub.api.domain.topic;

import forum.hub.api.domain.course.CourseCategory;

import java.time.LocalDateTime;
import java.util.Set;

public record TopicViewDTO(
        Long id,
        String title,
        String message,
        String author,
        String course,
        Set<CourseCategory> categories,
        LocalDateTime creationDate,
        Boolean solved) {


    public TopicViewDTO(Topic topic) {
        this(topic.getId(), topic.getTitle(), topic.getMessage().substring(0, Math.min(topic.getMessage().length(), 100)),
                topic.getAuthor().getName(), topic.getCourse().getName(), topic.getCourse().getCategories(), topic.getCreationDate(), topic.getSolved());
    }
}
