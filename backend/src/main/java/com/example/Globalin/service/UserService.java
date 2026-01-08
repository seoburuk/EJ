package com.example.Globalin.service;

import com.example.Globalin.dao.CommentDao;
import com.example.Globalin.dao.PostDao;
import com.example.Globalin.dao.UserDao;
import com.example.Globalin.dto.request.UpdateProfileRequest;
import com.example.Globalin.dto.response.UserProfileDTO;
import com.example.Globalin.model.Comment;
import com.example.Globalin.model.Post;
import com.example.Globalin.model.User;
import com.example.Globalin.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private CommentDao commentDao;

    /**
     * ユーザープロフィール取得（作成投稿/コメント含む）
     */
    public UserProfileDTO getUserProfile(Long userId) {
        logger.info("ユーザープロフィール取得 - userId: {}", userId);

        try {
            // ユーザー情報取得
            User user = userDao.findById(userId);
            if (user == null) {
                throw new RuntimeException("ユーザーが見つかりません。ID: " + userId);
            }

            // DTO作成
            UserProfileDTO profileDTO = new UserProfileDTO();
            profileDTO.setId(user.getId());
            profileDTO.setUsername(user.getUsername());
            profileDTO.setNickname(user.getNickname());
            profileDTO.setEmail(user.getEmail());
            profileDTO.setAvatar(user.getAvatar());
            profileDTO.setBio(user.getBio());
            profileDTO.setJoinDate(user.getJoinDate());
            profileDTO.setLastLoginDate(user.getLastLoginDate());
            profileDTO.setStatus(user.getStatus());

            // 最近の投稿取得（最大10件）
            List<Post> recentPosts = postDao.findByAuthorId(userId, 0, 10);
            profileDTO.setRecentPosts(recentPosts);
            profileDTO.setPostCount(recentPosts.size()); // 実際には全投稿数を取得すべき

            // 最近のコメント取得（最大10件）
            List<Comment> recentComments = commentDao.findByAuthorId(userId, 0, 10);
            profileDTO.setRecentComments(recentComments);
            profileDTO.setCommentCount(commentDao.countByAuthorId(userId));

            return profileDTO;

        } catch (Exception e) {
            logger.error("ユーザープロフィール取得失敗 - userId: {}, error: {}", userId, e.getMessage());
            throw new RuntimeException("プロフィール取得中にエラーが発生しました", e);
        }
    }

    public UserProfile getUserProfile(String userId) {
        // TODO: 実際のデータベース連携時に実装
        // 仮のモックデータを返却
        UserProfile profile = new UserProfile();
        profile.setId(1L);
        profile.setUsername("student123");
        profile.setEmail("student@example.com");
        profile.setNickname("留学生");
        profile.setPostCount(15);
        profile.setCommentCount(42);
        profile.setJoinDate(new java.util.Date());

        return profile;
    }

    /**
     * ユーザープロフィール更新
     */
    public UserProfileDTO updateProfile(Long userId, UpdateProfileRequest request) {
        logger.info("ユーザープロフィール更新 - userId: {}", userId);

        try {
            // ユーザー取得
            User user = userDao.findById(userId);
            if (user == null) {
                throw new RuntimeException("ユーザーが見つかりません。ID: " + userId);
            }

            // メール重複チェック（変更された場合のみ）
            if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
                int emailCount = userDao.countByEmail(request.getEmail());
                if (emailCount > 0) {
                    throw new RuntimeException("既に使用中のメールアドレスです");
                }
                user.setEmail(request.getEmail());
            }

            // ニックネーム更新
            if (request.getNickname() != null && !request.getNickname().trim().isEmpty()) {
                user.setNickname(request.getNickname());
            }

            // アバター更新
            if (request.getAvatar() != null) {
                user.setAvatar(request.getAvatar());
            }

            // 自己紹介更新
            if (request.getBio() != null) {
                user.setBio(request.getBio());
            }

            // データベース更新
            userDao.updateUser(user);
            logger.info("ユーザープロフィール更新完了 - userId: {}", userId);

            // 更新されたプロフィール返却
            return getUserProfile(userId);

        } catch (Exception e) {
            logger.error("ユーザープロフィール更新失敗 - userId: {}, error: {}", userId, e.getMessage());
            throw new RuntimeException("プロフィール更新中にエラーが発生しました: " + e.getMessage(), e);
        }
    }
}
