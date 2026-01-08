package com.example.Globalin.dto.request;

public class CreateCommentRequest {
    private Long postId;
    private Long parentId;  // null이면 게시글에 대한 댓글, 값이 있으면 대댓글
    private String content;
    private Boolean isAnonymous;

    // Getters and Setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
}
