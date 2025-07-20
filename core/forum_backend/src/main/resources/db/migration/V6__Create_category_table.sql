-- V6__Create_category_table.sql
CREATE TABLE forumdb.categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

ALTER TABLE forumdb.topics
ADD COLUMN category_id BIGINT,
ADD CONSTRAINT fk_topics_category FOREIGN KEY (category_id) REFERENCES forumdb.categories(id);

INSERT INTO forumdb.categories (name) VALUES ('Programming'), ('Sports'), ('Music'), ('Gaming'), ('Science');
