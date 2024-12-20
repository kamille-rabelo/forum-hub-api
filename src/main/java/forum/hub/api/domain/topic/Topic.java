package forum.hub.api.domain.topic;

import forum.hub.api.domain.answer.Answer;
import forum.hub.api.domain.course.Course;
import forum.hub.api.domain.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String message;
    private LocalDateTime creationDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
    private Boolean solved;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @OneToMany(mappedBy = "topic", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<>();

    public Topic() {
    }

    public Topic(Long id, String title, String message, LocalDateTime creationDate, User author, Boolean solved, Course course, List<Answer> answers) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.creationDate = creationDate;
        this.author = author;
        this.solved = solved;
        this.course = course;
        this.answers = answers;
    }

    public Topic(TopicCreateDTO data, User author, Course course) {
        this.title = data.title();
        this.message = data.message();
        this.creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        this.author = author;
        this.course = course;
        this.solved = false;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public User getAuthor() {
        return author;
    }

    public Boolean getSolved() {
        return solved;
    }

    public Course getCourse() {
        return course;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Topic topic)) return false;
        return Objects.equals(id, topic.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void update(TopicUpdateDTO data, Course course) {
        if (data.title() != null && !data.title().isEmpty()) this.title = data.title();
        if (data.message() != null && !data.message().isEmpty()) this.message = data.message();
        this.course = course != null ? course : this.course;
    }

    public void setTopicAsSolved() {
        solved = true;
    }
}
