CREATE TABLE votes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    topic_id BIGINT,
    reply_id BIGINT,
    is_upvote BOOLEAN NOT NULL,
    created_on TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_votes_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_votes_topic FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE,
    CONSTRAINT fk_votes_reply FOREIGN KEY (reply_id) REFERENCES replies(id) ON DELETE CASCADE,
    
    -- Ensure vote is for either topic OR reply, not both
    CONSTRAINT chk_vote_target CHECK (
        (topic_id IS NOT NULL AND reply_id IS NULL) OR 
        (topic_id IS NULL AND reply_id IS NOT NULL)
    ),
    
    -- Unique constraint: user can only vote once per topic
    CONSTRAINT uq_user_topic_vote UNIQUE (user_id, topic_id),
    
    -- Unique constraint: user can only vote once per reply
    CONSTRAINT uq_user_reply_vote UNIQUE (user_id, reply_id)
);
