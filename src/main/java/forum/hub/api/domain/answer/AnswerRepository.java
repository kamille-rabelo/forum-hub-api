package forum.hub.api.domain.answer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByIdAndTopicId(Long answerId, Long topicId);
}
