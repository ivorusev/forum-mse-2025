CREATE TABLE forumdb.replies (
    id BIGSERIAL PRIMARY KEY,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    topic_id BIGINT,
    user_id BIGINT,
    reply_body TEXT NOT NULL
);
