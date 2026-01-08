package com.example.Globalin.dto;

import com.example.Globalin.model.Post;

import java.util.List;
import java.util.Map;

public class MainPageDTO {
    private UserProfile userProfile;
    private List<Post> humanitiesPosts;
    private List<Post> freePosts;
    private List<HotPost> hotPosts;
    private List<Board> bestBoards;
    private List<BoardWithPosts> allBoards;

    // Inner classes for dashboard data
    public static class UserProfile {
        private String nickname;
        private Integer postCount;
        private Integer commentCount;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Integer getPostCount() {
            return postCount;
        }

        public void setPostCount(Integer postCount) {
            this.postCount = postCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }
    }

    public static class HotPost {
        private Long id;
        private String title;
        private Integer viewCount;
        private Integer likeCount;
        private Integer commentCount;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getViewCount() {
            return viewCount;
        }

        public void setViewCount(Integer viewCount) {
            this.viewCount = viewCount;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }
    }

    public static class Board {
        private Long id;
        private String name;
        private Integer postCount;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPostCount() {
            return postCount;
        }

        public void setPostCount(Integer postCount) {
            this.postCount = postCount;
        }
    }

    public static class BoardWithPosts {
        private Long id;
        private String name;
        private String description;
        private String category;
        private String icon;
        private Integer postCount;
        private List<Post> recentPosts;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Integer getPostCount() {
            return postCount;
        }

        public void setPostCount(Integer postCount) {
            this.postCount = postCount;
        }

        public List<Post> getRecentPosts() {
            return recentPosts;
        }

        public void setRecentPosts(List<Post> recentPosts) {
            this.recentPosts = recentPosts;
        }
    }

    // Getters and Setters
    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public List<Post> getHumanitiesPosts() {
        return humanitiesPosts;
    }

    public void setHumanitiesPosts(List<Post> humanitiesPosts) {
        this.humanitiesPosts = humanitiesPosts;
    }

    public List<Post> getFreePosts() {
        return freePosts;
    }

    public void setFreePosts(List<Post> freePosts) {
        this.freePosts = freePosts;
    }

    public List<HotPost> getHotPosts() {
        return hotPosts;
    }

    public void setHotPosts(List<HotPost> hotPosts) {
        this.hotPosts = hotPosts;
    }

    public List<Board> getBestBoards() {
        return bestBoards;
    }

    public void setBestBoards(List<Board> bestBoards) {
        this.bestBoards = bestBoards;
    }

    public List<BoardWithPosts> getAllBoards() {
        return allBoards;
    }

    public void setAllBoards(List<BoardWithPosts> allBoards) {
        this.allBoards = allBoards;
    }
}
