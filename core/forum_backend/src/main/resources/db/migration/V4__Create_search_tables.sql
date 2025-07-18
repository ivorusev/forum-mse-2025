-- Search results table 
CREATE TABLE IF NOT EXISTS forum.search_results (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    content TEXT,
    category VARCHAR(100) NOT NULL,
    relevance_score DECIMAL(3,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for search_results
CREATE INDEX IF NOT EXISTS idx_search_results_title ON forum.search_results (title);
CREATE INDEX IF NOT EXISTS idx_search_results_category ON forum.search_results (category);
CREATE INDEX IF NOT EXISTS idx_search_results_relevance_score ON forum.search_results (relevance_score);

-- Search suggestions table 
CREATE TABLE IF NOT EXISTS forum.search_suggestions (
    id BIGINT PRIMARY KEY,
    text VARCHAR(255) NOT NULL UNIQUE,
    category VARCHAR(100) NOT NULL,
    popularity INT DEFAULT 0,
    is_popular BOOLEAN DEFAULT FALSE
);

-- Create indexes for search_suggestions
CREATE INDEX IF NOT EXISTS idx_search_suggestions_text ON forum.search_suggestions (text);
CREATE INDEX IF NOT EXISTS idx_search_suggestions_popularity ON forum.search_suggestions (popularity);

-- Recent searches table 
CREATE TABLE IF NOT EXISTS forum.recent_searches (
    id BIGINT PRIMARY KEY,
    user_identifier VARCHAR(255) NOT NULL,
    query VARCHAR(255) NOT NULL,
    search_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for recent_searches
CREATE INDEX IF NOT EXISTS idx_recent_searches_user_identifier ON forum.recent_searches (user_identifier);
CREATE INDEX IF NOT EXISTS idx_recent_searches_search_time ON forum.recent_searches (search_time);

-- Search analytics table 
CREATE TABLE IF NOT EXISTS forum.search_analytics (
    id BIGINT PRIMARY KEY,
    query VARCHAR(255) NOT NULL,
    search_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    result_count INT DEFAULT 0,
    user_identifier VARCHAR(255)
);

-- Create indexes for search_analytics
CREATE INDEX IF NOT EXISTS idx_search_analytics_query ON forum.search_analytics (query);
CREATE INDEX IF NOT EXISTS idx_search_analytics_search_time ON forum.search_analytics (search_time);

-- Insert sample data
INSERT INTO forum.search_results (id, title, description, content, category, relevance_score) VALUES
(1, 'Java Spring Boot Tutorial', 'Complete guide to building REST APIs with Spring Boot', 'Learn how to create modern Java applications...', 'Topics', 0.95),
(2, 'React TypeScript Best Practices', 'Modern patterns for React development with TypeScript', 'Discover the best practices for writing...', 'Replies', 0.90),
(3, 'Database Design Principles', 'Fundamentals of relational database design', 'Understanding normalization, indexing...', 'Topics', 0.88),
(4, 'API Security Guidelines', 'Secure your REST APIs with these proven techniques', 'Authentication, authorization, and...', 'Replies', 0.92),
(5, 'Performance Optimization Tips', 'Boost your application performance', 'Learn about caching, database optimization...', 'Replies', 0.85);

INSERT INTO forum.search_suggestions (id, text, category, popularity, is_popular) VALUES
(1, 'java tutorial', 'Topics', 95, TRUE),
(2, 'react guide', 'Topics', 88, TRUE),
(3, 'database design', 'Topics', 82, TRUE),
(4, 'api security', 'Replies', 90, TRUE),
(5, 'performance optimization', 'Topics', 85, TRUE),
(6, 'spring boot', 'Topics', 92, TRUE),
(7, 'typescript', 'Topics', 78, FALSE),
(8, 'rest api', 'API', 89, TRUE);