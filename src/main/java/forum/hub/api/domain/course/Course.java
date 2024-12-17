package forum.hub.api.domain.course;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "courses_categories",
            joinColumns = @JoinColumn(name = "course_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Set<CourseCategory> categories = new HashSet<>();

    public Course() {
    }

    public Course(Long id, String name, Set<CourseCategory> categories) {
        this.id = id;
        this.name = name;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<CourseCategory> getCategories() {
        return categories;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Course course)) return false;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categories=" + categories +
                '}';
    }
}
