CREATE TABLE courses_categories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    category VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_courses_categories_course_id FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE (course_id, category)
);