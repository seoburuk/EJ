package com.example.Globalin.controller;

import com.example.Globalin.model.Notification;
import com.example.Globalin.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 通知リスト照会
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Notification> notifications = notificationService.getNotifications(userId, page, pageSize);
        return ResponseEntity.ok(notifications);
    }

    /**
     * 未読通知数の照会
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Integer>> getUnreadCount(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int count = notificationService.getUnreadCount(userId);
        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * 通知既読処理
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 全通知既読処理
     */
    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 通知削除
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }
}
