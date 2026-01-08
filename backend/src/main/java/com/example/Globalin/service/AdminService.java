package com.example.Globalin.service;

import com.example.Globalin.dao.UserDao;
import com.example.Globalin.dao.PostDao;
import com.example.Globalin.dao.CommentDao;
import com.example.Globalin.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private CommentDao commentDao;

    /**
     * ダッシュボード統計取得
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // 全ユーザー数
        int totalUsers = userDao.countAllUsers();

        // 全投稿数
        int totalPosts = postDao.countAllPosts();

        // 全コメント数
        int totalComments = commentDao.countAllComments();

        stats.put("totalUsers", totalUsers);
        stats.put("totalPosts", totalPosts);
        stats.put("totalComments", totalComments);
        stats.put("success", true);

        return stats;
    }

    /**
     * 管理者権限確認
     */
    public boolean isAdmin(Long userId) {
        if (userId == null) {
            return false;
        }

        User user = userDao.findById(userId);
        return user != null && "ADMIN".equals(user.getRole());
    }
}
