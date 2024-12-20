package forum.hub.api.domain.answer;

import forum.hub.api.domain.exception.ValidationException;
import forum.hub.api.domain.topic.TopicRepository;
import forum.hub.api.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public Answer post(AnswerPostDTO data) {
        var author = userRepository.findById(data.authorId())
                .orElseThrow(() -> new ValidationException("User id does not exist"));
        var topic = topicRepository.findById(data.topicId())
                .orElseThrow(() -> new ValidationException("Topic id does not exist"));

        return answerRepository.save(new Answer(data, author, topic));
    }

}
