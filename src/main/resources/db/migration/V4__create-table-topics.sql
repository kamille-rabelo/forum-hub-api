CREATE TABLE topics (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    solved BOOLEAN DEFAULT FALSE,

    PRIMARY KEY (id),
    CONSTRAINT fk_topics_user_id FOREIGN KEY (user_id) REFERENCES users(id)  ON DELETE CASCADE,
    CONSTRAINT fk_topics_course_id FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);