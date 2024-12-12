CREATE TABLE answers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    message TEXT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    solution BOOLEAN DEFAULT FALSE,

    PRIMARY KEY (id),
    CONSTRAINT fk_answers_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_answers_topic_id FOREIGN KEY (topic_id) REFERENCES topics(id)  ON DELETE CASCADE
);