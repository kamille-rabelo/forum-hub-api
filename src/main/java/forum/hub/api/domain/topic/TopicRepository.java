package forum.hub.api.domain.topic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByTitle(String title);

    @Query("SELECT t FROM Topic t WHERE LOWER(t.course.name) LIKE LOWER(:course) AND YEAR(t.creationDate) = :year")
    Page<Topic> listByCourseNameAndYear(String course, int year, Pageable pageable);

    @Query("SELECT t FROM Topic t WHERE LOWER(t.course.name) LIKE LOWER(:course)")
    Page<Topic> listByCourseName(String course, Pageable pageable);

    @Query("SELECT t FROM Topic t WHERE YEAR(t.creationDate) = :year")
    Page<Topic> listByYear(int year, Pageable pageable);
}
