-- 알림 테이블
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '알림을 받을 사용자 ID',
    type VARCHAR(50) NOT NULL COMMENT '알림 타입 (COMMENT, LIKE, REPLY 등)',
    related_id BIGINT COMMENT '관련 항목 ID (게시글 ID, 댓글 ID 등)',
    actor_id BIGINT COMMENT '알림을 발생시킨 사용자 ID',
    actor_name VARCHAR(100) COMMENT '알림을 발생시킨 사용자 이름',
    message TEXT NOT NULL COMMENT '알림 메시지',
    is_read BOOLEAN DEFAULT FALSE COMMENT '읽음 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '알림 생성 시간',
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at DESC),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='알림';
