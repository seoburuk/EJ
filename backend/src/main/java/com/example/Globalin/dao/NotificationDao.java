package com.example.Globalin.dao;

import com.example.Globalin.model.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationDao {

    // 알림 생성
    void insertNotification(Notification notification);

    // 사용자별 알림 목록 조회
    List<Notification> findByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

    // 읽지 않은 알림 수 조회
    int countUnreadByUserId(@Param("userId") Long userId);

    // 알림 읽음 처리
    void markAsRead(@Param("id") Long id);

    // 모든 알림 읽음 처리
    void markAllAsRead(@Param("userId") Long userId);

    // 알림 삭제
    void deleteNotification(@Param("id") Long id);

    // 특정 ID의 알림 조회
    Notification findById(@Param("id") Long id);
}
