package com.example.Globalin.model;

import java.util.Date;

/**
 * 채팅 메시지 엔티티
 * 전체 채팅방의 메시지를 저장하는 모델
 */
public class ChatMessage {
    private Long id;
    private String username;  // 메시지를 보낸 사용자명
    private String message;   // 메시지 내용
    private Date createdAt;   // 메시지 작성 시간

    // 기본 생성자
    public ChatMessage() {
    }

    // 전체 필드 생성자
    public ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
        this.createdAt = new Date();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
