package forum.hub.api.domain.topic;

import forum.hub.api.domain.answer.AnswerViewDTO;
import forum.hub.api.domain.course.CourseCategory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record TopicDetailDTO(
        Long id,
        String title,
        String message,
        String author,
        List<AnswerViewDTO> answers,
        String course,
        Set<CourseCategory> categories,
        LocalDateTime creationDate,
        Boolean solved) {

    public TopicDetailDTO(Topic topic) {
        this(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getAuthor().getName(), topic.getAnswers().stream().map(AnswerViewDTO::new).toList(),
                topic.getCourse().getName(), topic.getCourse().getCategories(), topic.getCreationDate(), topic.getSolved());
    }
}
