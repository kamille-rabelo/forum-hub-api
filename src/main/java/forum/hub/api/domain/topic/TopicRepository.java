package forum.hub.api.domain.topic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Query("""
    SELECT t FROM Topic t
    WHERE (:course IS NULL OR LOWER(t.course.name) LIKE LOWER(CONCAT('%', :course, '%')))
      AND (:year IS NULL OR YEAR(t.creationDate) = :year)
    """)
    Page<Topic> listByCourseNameAndYear(String course, Integer year, Pageable pageable);

    @Query("SELECT t FROM Topic t WHERE LOWER(t.course.name) LIKE LOWER(:course)")
    Page<Topic> listByCourseName(String course, Pageable pageable);

    @Query("SELECT t FROM Topic t WHERE YEAR(t.creationDate) = :year")
    Page<Topic> listByYear(int year, Pageable pageable);

    Optional<Topic> findByTitleAndMessage(String title, String message);
}
