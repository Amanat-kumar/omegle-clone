-- Create app_user Table (Renamed from user to avoid H2 reserved keyword)
CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create ChatSession Table for random video chat
CREATE TABLE IF NOT EXISTS chat_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_1_id BIGINT NOT NULL,
    user_2_id BIGINT NOT NULL,
    status VARCHAR(50),  -- We are just creating the column, without a default value
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP,
    FOREIGN KEY (user_1_id) REFERENCES app_user(id),
    FOREIGN KEY (user_2_id) REFERENCES app_user(id)
);


-- Create Messages Table to store text-based messages
CREATE TABLE IF NOT EXISTS messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chat_session_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_session_id) REFERENCES chat_sessions(id),
    FOREIGN KEY (sender_id) REFERENCES app_user(id)
);

-- Create Reports Table for Inappropriate User Behavior
CREATE TABLE IF NOT EXISTS reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reported_by_id BIGINT NOT NULL,
    reported_user_id BIGINT NOT NULL,
    reason VARCHAR(255) NOT NULL,
    report_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reported_by_id) REFERENCES app_user(id),
    FOREIGN KEY (reported_user_id) REFERENCES app_user(id)
);

-- Insert Two Dummy Users for Testing
INSERT INTO app_user (username, email, password_hash) 
VALUES 
('testuser1', 'testuser1@example.com', 'hashedpassword1'), 
('testuser2', 'testuser2@example.com', 'hashedpassword2');
