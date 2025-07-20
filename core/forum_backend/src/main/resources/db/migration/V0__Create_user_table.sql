CREATE SCHEMA IF NOT EXISTS forumdb;

CREATE TABLE forumdb.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert a default user for testing purposes
INSERT INTO forumdb.users (username, email, password, role) VALUES ('testuser', 'testuser@example.com', 'password', 'USER');
