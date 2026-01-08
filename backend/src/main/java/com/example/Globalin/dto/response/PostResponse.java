package com.example.Globalin.dto.response;

public class PostResponse {
    private boolean success;
    private String message;
    private Long postId;

    public PostResponse() {
    }

    public PostResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public PostResponse(boolean success, String message, Long postId) {
        this.success = success;
        this.message = message;
        this.postId = postId;
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

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
