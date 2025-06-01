CREATE SEQUENCE IF NOT EXISTS reply_entity_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE reply_entity
(
    id          BIGINT       NOT NULL,
    topic_id    BIGINT,
    user_id     BIGINT,
    reply_body  VARCHAR(255) NOT NULL,
    created_on  TIMESTAMP WITHOUT TIME ZONE,
    modified_on TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_replyentity PRIMARY KEY (id)
);