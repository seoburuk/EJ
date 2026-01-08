package com.example.Globalin.dto.response;

public class RegisterResponse {
    private boolean success;
    private String message;
    private Long userId;

    public RegisterResponse() {
    }

    public RegisterResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public RegisterResponse(boolean success, String message, Long userId) {
        this.success = success;
        this.message = message;
        this.userId = userId;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
