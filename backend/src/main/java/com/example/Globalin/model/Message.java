package com.example.Globalin.model;

import java.sql.Timestamp;

public class Message {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String title;
    private String content;
    private Boolean isRead;
    private Timestamp createdAt;
    private Boolean deletedBySender;
    private Boolean deletedByReceiver;

    // 추가 정보 (JOIN용)
    private String senderNickname;
    private String receiverNickname;

    public Message() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDeletedBySender() {
        return deletedBySender;
    }

    public void setDeletedBySender(Boolean deletedBySender) {
        this.deletedBySender = deletedBySender;
    }

    public Boolean getDeletedByReceiver() {
        return deletedByReceiver;
    }

    public void setDeletedByReceiver(Boolean deletedByReceiver) {
        this.deletedByReceiver = deletedByReceiver;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getReceiverNickname() {
        return receiverNickname;
    }

    public void setReceiverNickname(String receiverNickname) {
        this.receiverNickname = receiverNickname;
    }
}
