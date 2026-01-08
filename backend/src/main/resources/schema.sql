-- Users 테이블 생성
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    nickname VARCHAR(50) NOT NULL,
    avatar VARCHAR(255),
    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_date TIMESTAMP NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
