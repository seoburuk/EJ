package com.example.Globalin.dto.response;

public class CommentResponse {
    private boolean success;
    private String message;
    private Long commentId;
    private Boolean isLiked;

    public CommentResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public CommentResponse(boolean success, String message, Long commentId) {
        this.success = success;
        this.message = message;
        this.commentId = commentId;
    }

    public CommentResponse(boolean success, String message, Boolean isLiked) {
        this.success = success;
        this.message = message;
        this.isLiked = isLiked;
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

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }
}
