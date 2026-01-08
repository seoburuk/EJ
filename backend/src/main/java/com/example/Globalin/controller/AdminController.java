package com.example.Globalin.controller;

import com.example.Globalin.model.User;
import com.example.Globalin.model.Post;
import com.example.Globalin.model.Comment;
import com.example.Globalin.service.AdminService;
import com.example.Globalin.dao.UserDao;
import com.example.Globalin.dao.PostDao;
import com.example.Globalin.dao.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private CommentDao commentDao;

    /**
     * 管理者権限確認ヘルパーメソッド
     */
    private boolean isAdmin(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return adminService.isAdmin(userId);
    }

    /**
     * 管理者ダッシュボード統計
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getStats(HttpSession session) {
        if (!isAdmin(session)) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "管理者権限が必要です。");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        Map<String, Object> stats = adminService.getStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 全ユーザーリスト照会
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(HttpSession session,
                                         @RequestParam(defaultValue = "0") int offset,
                                         @RequestParam(defaultValue = "50") int limit) {
        if (!isAdmin(session)) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "管理者権限が必要です。");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        List<User> users = userDao.findAllUsers(offset, limit);
        int totalUsers = userDao.countAllUsers();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("users", users);
        response.put("total", totalUsers);

        return ResponseEntity.ok(response);
    }

    /**
     * ユーザー状態変更 (ACTIVE, INACTIVE, BANNED)
     */
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request,
            HttpSession session) {

        if (!isAdmin(session)) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "管理者権限が必要です。");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        String newStatus = request.get("status");
        userDao.updateUserStatus(userId, newStatus);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "ユーザー状態が変更されました。");

        return ResponseEntity.ok(response);
    }

    /**
     * 全投稿リスト照会
     */
    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts(HttpSession session,
                                         @RequestParam(defaultValue = "0") int offset,
                                         @RequestParam(defaultValue = "50") int limit) {
        if (!isAdmin(session)) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "管理者権限が必要です。");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        List<Post> posts = postDao.findAll(offset, limit);
        int totalPosts = postDao.countAllPosts();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("posts", posts);
        response.put("total", totalPosts);

        return ResponseEntity.ok(response);
    }

    /**
     * 投稿強制削除
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long postId,
            HttpSession session) {

        if (!isAdmin(session)) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "管理者権限が必要です。");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        // TODO: 投稿削除ロジック実装
        postDao.deletePost(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "投稿が削除されました。");

        return ResponseEntity.ok(response);
    }

    /**
     * コメント強制削除
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            HttpSession session) {

        if (!isAdmin(session)) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "管理者権限が必要です。");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        commentDao.deleteComment(commentId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "コメントが削除されました。");

        return ResponseEntity.ok(response);
    }
}
