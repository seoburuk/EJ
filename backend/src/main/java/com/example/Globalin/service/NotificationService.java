package com.example.Globalin.service;

import com.example.Globalin.dao.NotificationDao;
import com.example.Globalin.model.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationDao notificationDao;

    /**
     * 通知作成
     */
    @Transactional
    public void createNotification(Long userId, String type, Long relatedId, Long actorId, String actorName, String message) {
        try {
            // 自分自身には通知を送らない
            if (userId.equals(actorId)) {
                return;
            }

            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setType(type);
            notification.setRelatedId(relatedId);
            notification.setActorId(actorId);
            notification.setActorName(actorName);
            notification.setMessage(message);

            notificationDao.insertNotification(notification);
            logger.info("通知作成完了 - userId: {}, type: {}", userId, type);

        } catch (Exception e) {
            logger.error("通知作成失敗 - userId: {}, error: {}", userId, e.getMessage());
        }
    }

    /**
     * ユーザー別通知一覧取得
     */
    public List<Notification> getNotifications(Long userId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return notificationDao.findByUserId(userId, offset, pageSize);
    }

    /**
     * 未読通知数取得
     */
    public int getUnreadCount(Long userId) {
        return notificationDao.countUnreadByUserId(userId);
    }

    /**
     * 通知を読み込み済みにマーク
     */
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationDao.markAsRead(notificationId);
    }

    /**
     * すべての通知を読み込み済みにマーク
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationDao.markAllAsRead(userId);
    }

    /**
     * 通知削除
     */
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationDao.deleteNotification(notificationId);
    }
}
