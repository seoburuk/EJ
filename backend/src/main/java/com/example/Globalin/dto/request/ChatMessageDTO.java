package com.example.Globalin.dto.request;

/**
 * 채팅 메시지 요청 DTO
 * WebSocket을 통해 클라이언트로부터 받는 메시지 데이터
 */
public class ChatMessageDTO {
    private String username;  // 메시지를 보낸 사용자명
    private String message;   // 메시지 내용

    // 기본 생성자
    public ChatMessageDTO() {
    }

    // 전체 필드 생성자
    public ChatMessageDTO(String username, String message) {
        this.username = username;
        this.message = message;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "ChatMessageDTO{" +
                "username='" + username + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
