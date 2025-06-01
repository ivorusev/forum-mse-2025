CREATE SEQUENCE IF NOT EXISTS topic_entity_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE topic_entity
(
    id          BIGINT       NOT NULL,
    title       VARCHAR(255) NOT NULL,
    created_on  TIMESTAMP WITHOUT TIME ZONE,
    modified_on TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_topicentity PRIMARY KEY (id)
);

ALTER TABLE topic_entity
    ADD CONSTRAINT uc_topicentity_title UNIQUE (title);