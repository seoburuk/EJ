-- 채팅 메시지 테이블 생성
CREATE TABLE IF NOT EXISTS chat_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL COMMENT '사용자명',
    message TEXT NOT NULL COMMENT '채팅 메시지 내용',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '메시지 작성 시간',
    INDEX idx_created_at (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='전체 채팅방 메시지';
