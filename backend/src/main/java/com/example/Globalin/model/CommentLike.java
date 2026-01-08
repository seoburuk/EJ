package com.example.Globalin.model;

import java.sql.Timestamp;

public class CommentLike {
    private Long id;
    private Long commentId;
    private Long userId;
    private Timestamp createdAt;

    // Constructors
    public CommentLike() {
    }

    public CommentLike(Long commentId, Long userId) {
        this.commentId = commentId;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
