package forum.hub.api.domain.answer;

import forum.hub.api.domain.topic.Topic;
import forum.hub.api.domain.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
    private LocalDateTime creationDate;
    private Boolean solution;

    public Answer() {
    }

    public Answer(Long id, String message, User author, Topic topic, LocalDateTime creationDate, Boolean solution) {
        this.id = id;
        this.message = message;
        this.author = author;
        this.topic = topic;
        this.creationDate = creationDate;
        this.solution = solution;
    }

    public Answer(AnswerPostDTO data, User author, Topic topic) {
        this.message = data.message();
        this.author = author;
        this.topic = topic;
        this.creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        this.solution = false;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public User getAuthor() {
        return author;
    }

    public Topic getTopic() {
        return topic;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Boolean getSolution() {
        return solution;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Answer answer)) return false;
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
