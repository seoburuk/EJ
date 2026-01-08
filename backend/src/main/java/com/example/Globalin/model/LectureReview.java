package com.example.Globalin.model;

import java.time.LocalDateTime;

public class LectureReview {
    private Long id;
    private Long userId;
    private String courseName;
    private String professor;
    private Integer difficulty;     // 1-5
    private Integer workload;       // 1-5
    private Integer satisfaction;   // 1-5
    private String reviewText;
    private String semester;
    private Integer year;
    private Boolean isAnonymous;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 조인용 추가 필드
    private String authorNickname;

    public LectureReview() {
    }

    public LectureReview(Long userId, String courseName, String professor,
                        Integer difficulty, Integer workload, Integer satisfaction,
                        String reviewText, String semester, Integer year, Boolean isAnonymous) {
        this.userId = userId;
        this.courseName = courseName;
        this.professor = professor;
        this.difficulty = difficulty;
        this.workload = workload;
        this.satisfaction = satisfaction;
        this.reviewText = reviewText;
        this.semester = semester;
        this.year = year;
        this.isAnonymous = isAnonymous;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getWorkload() {
        return workload;
    }

    public void setWorkload(Integer workload) {
        this.workload = workload;
    }

    public Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Integer satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }

    @Override
    public String toString() {
        return "LectureReview{" +
                "id=" + id +
                ", userId=" + userId +
                ", courseName='" + courseName + '\'' +
                ", professor='" + professor + '\'' +
                ", difficulty=" + difficulty +
                ", workload=" + workload +
                ", satisfaction=" + satisfaction +
                ", semester='" + semester + '\'' +
                ", year=" + year +
                ", isAnonymous=" + isAnonymous +
                ", createdAt=" + createdAt +
                '}';
    }
}
