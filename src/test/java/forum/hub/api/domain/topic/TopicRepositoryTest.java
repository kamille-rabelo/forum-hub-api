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

    private static final LocalDateTime CURRENT_DATE = LocalDateTime.now();
    private static final int CURRENT_YEAR = CURRENT_DATE.getYear();

    @Test
    void shouldReturnTopicsWhenGivenCourseAndYear() {
        var author = createAndPersistUser();
        var course = createAndPersistCourse("Course");

        createAndPersistTopic(author, course, CURRENT_DATE, "Topic Title 1");
        createAndPersistTopic(author, course, CURRENT_DATE, "Topic Title 2");

        var topics = topicRepository.listByCourseNameAndYear("course", CURRENT_YEAR, Pageable.unpaged());

        assertThat(topics)
                .hasSize(2)
                .extracting("title")
                .containsExactlyInAnyOrder("Topic Title 1", "Topic Title 2");
    }

    @Test
    void shouldReturnEmptyListWhenNoRegisteredTopicsForCourseAndYear() {
        var user = createAndPersistUser();
        var firstCourse = createAndPersistCourse("First");
        var secondCourse = createAndPersistCourse("Second");
        var oldDate = LocalDateTime.of(2005, 1, 1, 12, 0);

        createAndPersistTopic(user, firstCourse, oldDate, "Old Topic");
        createAndPersistTopic(user, secondCourse, CURRENT_DATE, "Current Topic");

        var topics = topicRepository.listByCourseNameAndYear("first", CURRENT_YEAR, Pageable.unpaged());

        assertThat(topics).isEmpty();
    }

    @Test
    void shouldReturnAllTopicsWhenCourseAndYearAreNull() {
        var author = createAndPersistUser();
        var course = createAndPersistCourse("Course");

        createAndPersistTopic(author, course, CURRENT_DATE, "Topic Title 1");
        createAndPersistTopic(author, course, CURRENT_DATE, "Topic Title 2");

        var topics = topicRepository.listByCourseNameAndYear(null, null, Pageable.unpaged());

        assertThat(topics).hasSize(2);
    }

    @Test
    void shouldReturnTopicsWhenOnlyYearIsNull() {
        var author = createAndPersistUser();
        var firstCourse = createAndPersistCourse("First");
        var secondCourse = createAndPersistCourse("Second");

        createAndPersistTopic(author, firstCourse, CURRENT_DATE, "Topic Title 1");
        createAndPersistTopic(author, secondCourse, CURRENT_DATE, "Topic Title 2");

        var topics = topicRepository.listByCourseNameAndYear("second", null, Pageable.unpaged());

        assertThat(topics).hasSize(1);
    }

    @Test
    void shouldReturnTopicsWhenOnlyCourseIsNull() {
        var author = createAndPersistUser();
        var course = createAndPersistCourse("Course");
        var oldDate = LocalDateTime.of(2005, 1, 1, 12, 0);

        createAndPersistTopic(author, course, CURRENT_DATE, "Topic Title 1");
        createAndPersistTopic(author, course, oldDate, "Topic Title 2");

        var topics = topicRepository.listByCourseNameAndYear(null, CURRENT_YEAR, Pageable.unpaged());

        assertThat(topics).hasSize(1);
    }

    private void createAndPersistTopic(User author, Course course, LocalDateTime date, String title) {
        var topic = new Topic(null, title, "Sample Message", date, author, false, course, null);
        entityManager.persist(topic);
    }

    private User createAndPersistUser() {
        var user = new User(null, "User", "user@example.com", "Password123", Role.STUDENT);
        return entityManager.persist(user);
    }

    private Course createAndPersistCourse(String name) {
        var course = new Course(null, name, Set.of(CourseCategory.DATASCIENCE, CourseCategory.PYTHON));
        return entityManager.persist(course);
    }
}
