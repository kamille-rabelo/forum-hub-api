package forum.hub.api.domain.topic;

import forum.hub.api.domain.course.Course;
import forum.hub.api.domain.course.CourseCategory;
import forum.hub.api.domain.course.CourseRepository;
import forum.hub.api.domain.user.Role;
import forum.hub.api.domain.user.User;
import forum.hub.api.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final int CURRENT_YEAR = LocalDateTime.now().getYear();

    @Test
    void shouldReturnTopicsWhenGivenCourseAndYear() {
        var author = createUser();
        var course = createCourse("Course");
        var date = LocalDateTime.now();

        createTopic(author, course, date, "Topic Title 1");
        createTopic(author, course, date, "Topic Title 2");

        var topics = topicRepository.listByCourseNameAndYear("course", CURRENT_YEAR, Pageable.unpaged());

        assertThat(topics)
                .hasSize(2)
                .extracting("title")
                .containsExactlyInAnyOrder("Topic Title 1", "Topic Title 2");
    }

    @Test
    void shouldReturnEmptyListWhenNoRegisteredTopicsForCourseAndYear() {
        var user = createUser();
        var firstCourse = createCourse("First Course");
        var secondCourse = createCourse("Second");
        var oldDate = LocalDateTime.of(2005, 1, 1, 12, 0);
        var currentDate = LocalDateTime.now();

        createTopic(user, firstCourse, oldDate, "Old Topic");
        createTopic(user, secondCourse, currentDate, "Current Topic");

        var topics = topicRepository.listByCourseNameAndYear("course", CURRENT_YEAR, Pageable.unpaged());

        assertThat(topics).isEmpty();
    }

    private void createTopic(User author, Course course, LocalDateTime date, String title) {
        var topic = new Topic(null, title, "Sample Message", date, author, false, course, null);
        entityManager.persist(topic);
    }

    private User createUser() {
        var user = new User(null, "User", "user@example.com", "Password123", Role.STUDENT);
        return entityManager.persist(user);
    }

    private Course createCourse(String name) {
        var course = new Course(null, name, Set.of(CourseCategory.DATASCIENCE, CourseCategory.PYTHON));
        return entityManager.persist(course);
    }
}
