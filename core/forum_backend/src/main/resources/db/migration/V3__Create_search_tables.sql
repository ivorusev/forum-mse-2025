-- Search results table 
CREATE TABLE IF NOT EXISTS forum.search_results (id BIGINT NOT NULL,title VARCHAR(255) NOT NULL,description TEXT,content TEXT,category VARCHAR(100) NOT NULL,relevance_score DECIMAL(3,2),created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,INDEX idx_title (title),INDEX idx_category (category),INDEX idx_relevance_score (relevance_score),FULLTEXT KEY idx_fulltext (title, description, content));

-- Search suggestions table 
CREATE TABLE IF NOT EXISTS forum.search_suggestions (id BIGINT NOT NULL,text VARCHAR(255) NOT NULL UNIQUE,category VARCHAR(100) NOT NULL,popularity INT DEFAULT 0,is_popular BOOLEAN DEFAULT FALSE,INDEX idx_text (text),INDEX idx_popularity (popularity));

-- Recent searches table 
CREATE TABLE IF NOT EXISTS forum.recent_searches (id BIGINT NOT NULL,user_identifier VARCHAR(255) NOT NULL,query VARCHAR(255) NOT NULL,search_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,INDEX idx_user_identifier (user_identifier),INDEX idx_search_time (search_time));

-- Search analytics table 
CREATE TABLE IF NOT EXISTS forum.search_analytics (id BIGINT NOT NULL,query VARCHAR(255) NOT NULL,search_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,result_count INT DEFAULT 0,user_identifier VARCHAR(255),INDEX idx_query (query),INDEX idx_search_time (search_time));

-- Insert sample data 
INSERT INTO forum.search_results (title, description, content, category, relevance_score) VALUES('Java Spring Boot Tutorial', 'Complete guide to building REST APIs with Spring Boot', 'Learn how to create modern Java applications...', 'Topics', 0.95),('React TypeScript Best Practices', 'Modern patterns for React development with TypeScript', 'Discover the best practices for writing...', 'Replies', 0.90),('Database Design Principles', 'Fundamentals of relational database design', 'Understanding normalization, indexing...', 'Topics', 0.88),('API Security Guidelines', 'Secure your REST APIs with these proven techniques', 'Authentication, authorization, and...', 'Replies', 0.92),('Performance Optimization Tips', 'Boost your application performance', 'Learn about caching, database optimization...', 'Replies', 0.85);
INSERT INTO forum.search_suggestions (text, category, popularity, is_popular) VALUES('java tutorial', 'Topics', 95, TRUE),('react guide', 'Topics', 88, TRUE),('database design', 'Topics', 82, TRUE),('api security', 'Replies', 90, TRUE),('performance optimization', 'Topics', 85, TRUE),('spring boot', 'Topics', 92, TRUE),('typescript', 'Topics', 78, FALSE),('rest api', 'API', 89, TRUE);