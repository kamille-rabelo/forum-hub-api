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
        var existingTopic = topicRepository.findByTitle(data.title());

        if (existingTopic.isPresent() && existingTopic.get().getMessage().equalsIgnoreCase(data.message())) {
            throw new ValidationException("Topic already exists");
        }

        var author = userRepository.findById(data.authorId())
                .orElseThrow(() -> new ValidationException("User id does not exist"));
        var course = courseRepository.findById(data.courseId())
                .orElseThrow(() -> new ValidationException("Course id does not exist"));

        var topic = new Topic(data, author, course);
        topicRepository.save(topic);

       return topic;
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
}
