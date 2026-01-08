package com.example.Globalin.model;

import java.sql.Timestamp;

public class PostImage {
    private Long id;
    private Long postId;
    private String imageUrl;
    private String originalFilename;
    private Long fileSize;
    private Integer displayOrder;
    private Timestamp createdAt;

    // Constructors
    public PostImage() {
    }

    public PostImage(Long postId, String imageUrl, String originalFilename, Long fileSize, Integer displayOrder) {
        this.postId = postId;
        this.imageUrl = imageUrl;
        this.originalFilename = originalFilename;
        this.fileSize = fileSize;
        this.displayOrder = displayOrder;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PostImage{" +
                "id=" + id +
                ", postId=" + postId +
                ", imageUrl='" + imageUrl + '\'' +
                ", originalFilename='" + originalFilename + '\'' +
                ", fileSize=" + fileSize +
                ", displayOrder=" + displayOrder +
                ", createdAt=" + createdAt +
                '}';
    }
}
