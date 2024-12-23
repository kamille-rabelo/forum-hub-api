package forum.hub.api.domain.topic;

import forum.hub.api.domain.answer.AnswerRepository;
import forum.hub.api.domain.course.CourseRepository;
import forum.hub.api.domain.exception.EntityNotFoundException;
import forum.hub.api.domain.exception.ValidationException;
import forum.hub.api.domain.user.UserRepository;
import forum.hub.api.infra.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AuthenticatedUserProvider userProvider;

    public TopicViewDTO create(TopicCreateDTO data) {
        var doesDuplicateExist = topicRepository.findByTitleAndMessage(data.title(), data.message()).isPresent();
        if (doesDuplicateExist) {
            throw new ValidationException("Topic already exists");
        }

        var author = userRepository.findById(data.authorId()).orElseThrow(
                () -> new EntityNotFoundException("User id does not exist"));
        var course = courseRepository.findById(data.courseId()).orElseThrow(
                () -> new EntityNotFoundException("Course id does not exist"));

        var topic = new Topic(data, author, course);
        topicRepository.save(topic);
        return new TopicViewDTO(topic);
    }

    public Page<TopicViewDTO> getTopics(String course, Year year, Pageable pageable) {
        return topicRepository.listByCourseNameAndYear(course, year.getValue(), pageable)
                .map(TopicViewDTO::new);
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Topic id does not exist"));
    }

    public Topic update(Long id, TopicUpdateDTO data) {
        var topic = getTopicById(id);

        if (!isAuthorizedToProceed(topic)) {
            throw new AccessDeniedException("You are not the author of this topic");
        }

        var course = data.courseId() == null ? null : courseRepository.findById(data.courseId())
                .orElseThrow(() -> new EntityNotFoundException("Course id does not exist"));

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
        var topic = getTopicById(id);

        if (!isAuthorizedToProceed(topic) && !userProvider.isAdmin()) {
            throw new AccessDeniedException("You are not the author of this topic");
        }

        topicRepository.deleteById(id);
    }

    private boolean isAuthorizedToProceed(Topic topic) {
        return userProvider.getUser().equals(topic.getAuthor());
    }

    public void markSolved(Long topicId, Long answerId) {
        var topic = getTopicById(topicId);

        var answer = answerRepository.findByIdAndTopicId(answerId, topicId).orElseThrow(
                () -> new ValidationException("Answer does not belong to topic or does not exist"));

        if (topic.getSolved()) {
            throw new ValidationException("Topic is already solved");
        }

        topic.setTopicAsSolved();
        answer.markAsSolution();
    }
}
