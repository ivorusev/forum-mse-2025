CREATE TABLE forumdb.votes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    topic_id BIGINT,
    reply_id BIGINT,
    is_upvote BOOLEAN NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_votes_user FOREIGN KEY (user_id) REFERENCES forumdb.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_votes_topic FOREIGN KEY (topic_id) REFERENCES forumdb.topics(id) ON DELETE CASCADE,
    CONSTRAINT fk_votes_reply FOREIGN KEY (reply_id) REFERENCES forumdb.replies(id) ON DELETE CASCADE,
    
    CONSTRAINT chk_vote_target CHECK (
        (topic_id IS NOT NULL AND reply_id IS NULL) OR 
        (topic_id IS NULL AND reply_id IS NOT NULL)
    ),
    
    CONSTRAINT uq_user_topic_vote UNIQUE (user_id, topic_id),
    CONSTRAINT uq_user_reply_vote UNIQUE (user_id, reply_id)
);
