package com.example.Globalin.dto.response;

import com.example.Globalin.model.Post;
import com.example.Globalin.model.Comment;

import java.util.Date;
import java.util.List;

/**
 * 사용자 프로필 응답 DTO
 * 사용자 정보 + 작성 글/댓글 목록을 포함
 */
public class UserProfileDTO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String avatar;
    private String bio;
    private Date joinDate;
    private Date lastLoginDate;
    private String status;

    // 통계 정보
    private int postCount;
    private int commentCount;

    // 최근 작성 글/댓글
    private List<Post> recentPosts;
    private List<Comment> recentComments;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<Post> getRecentPosts() {
        return recentPosts;
    }

    public void setRecentPosts(List<Post> recentPosts) {
        this.recentPosts = recentPosts;
    }

    public List<Comment> getRecentComments() {
        return recentComments;
    }

    public void setRecentComments(List<Comment> recentComments) {
        this.recentComments = recentComments;
    }
}
