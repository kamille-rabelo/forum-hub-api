package forum.hub.api.domain.topic;

import forum.hub.api.domain.answer.Answer;
import forum.hub.api.domain.course.Course;
import forum.hub.api.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "topics")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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
    @OneToMany(mappedBy = "topic", fetch = FetchType.EAGER)
    private List<Answer> answers = new ArrayList<>();

    public Topic(TopicCreateDTO data, User author, Course course) {
        this.title = data.title();
        this.message = data.message();
        this.creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        this.author = author;
        this.course = course;
        this.solved = false;
    }
}
