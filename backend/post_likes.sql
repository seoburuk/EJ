-- 게시글 좋아요 테이블
CREATE TABLE IF NOT EXISTS post_likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL COMMENT '게시글 ID',
    user_id BIGINT NOT NULL COMMENT '사용자 ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '좋아요 누른 시간',
    UNIQUE KEY uk_post_user (post_id, user_id),
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시글 좋아요';
