package forum.hub.api.domain.topic;

import forum.hub.api.domain.course.CourseRepository;
import forum.hub.api.domain.exception.ValidationException;
import forum.hub.api.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Topic create(TopicCreateDTO data) {
        var doesDuplicateExist = topicRepository.findByTitleAndMessage(data.title(), data.message()).isPresent();
        if (doesDuplicateExist) {
            throw new ValidationException("Topic already exists");
        }

        var author = userRepository.findById(data.authorId()).orElseThrow(
                () -> new ValidationException("User id does not exist"));
        var course = courseRepository.findById(data.courseId()).orElseThrow(
                () -> new ValidationException("Course id does not exist"));

        return topicRepository.save(new Topic(data, author, course));
    }

    public Page<Topic> getTopics(String course, Year year, Pageable pageable) {
        if (course != null && year != null) {
            return topicRepository.listByCourseNameAndYear("%" + course + "%", year.getValue(), pageable);
        }

        if (course != null) {
            return topicRepository.listByCourseName("%" + course + "%", pageable);
        }

        if (year != null) {
            return topicRepository.listByYear(year.getValue(), pageable);
        }

        return topicRepository.findAll(pageable);
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Topic id does not exist"));
    }

    public Topic update(Long id, TopicUpdateDTO data) {
        var topic = topicRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Topic id does not exist"));
        var course = data.courseId() == null ? null : courseRepository.findById(data.courseId())
                .orElseThrow(() -> new ValidationException("Course id does not exist"));

        if (data.title() != null || data.message() != null) {
            var doesDuplicateExist = topicRepository.findByTitleAndMessage(
                    data.title() == null ? topic.getTitle() : data.title(),
                    data.message() == null ? topic.getMessage() : data.message()
            ).isPresent();

            if (doesDuplicateExist) {
                throw new ValidationException("Topic already exists");
            }
        }

        topic.update(data, course);
        return topicRepository.save(topic);
    }

    public void delete(Long id) {
        if (topicRepository.existsById(id)) {
            topicRepository.deleteById(id);
        } else {
            throw new ValidationException("Topic id does not exist");
        }
    }
}
