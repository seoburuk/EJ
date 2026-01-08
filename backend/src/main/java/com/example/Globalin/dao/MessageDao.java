package com.example.Globalin.dao;

import com.example.Globalin.model.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageDao {
    // 메시지 전송
    void insertMessage(Message message);

    // 메시지 조회
    Message findById(Long id);

    // 받은 메시지 목록 조회
    List<Message> findReceivedMessages(@Param("userId") Long userId,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    // 보낸 메시지 목록 조회
    List<Message> findSentMessages(@Param("userId") Long userId,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);

    // 받은 메시지 수 조회
    int countReceivedMessages(@Param("userId") Long userId);

    // 보낸 메시지 수 조회
    int countSentMessages(@Param("userId") Long userId);

    // 안 읽은 메시지 수 조회
    int countUnreadMessages(@Param("userId") Long userId);

    // 메시지 읽음 처리
    void markAsRead(@Param("id") Long id);

    // 발신자가 삭제
    void deleteForSender(@Param("id") Long id);

    // 수신자가 삭제
    void deleteForReceiver(@Param("id") Long id);

    // 실제 삭제 (양쪽 모두 삭제한 경우)
    void deleteMessage(@Param("id") Long id);
}
