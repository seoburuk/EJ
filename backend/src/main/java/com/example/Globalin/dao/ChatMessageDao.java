package com.example.Globalin.dao;

import com.example.Globalin.model.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 채팅 메시지 DAO
 * MyBatis를 통해 chat_messages 테이블에 접근
 */
@Mapper
public interface ChatMessageDao {

    /**
     * 채팅 메시지 저장
     */
    void insertMessage(ChatMessage chatMessage);

    /**
     * 최근 메시지 조회 (개수 제한)
     */
    List<ChatMessage> findRecentMessages(@Param("limit") int limit);

    /**
     * 모든 메시지 조회
     */
    List<ChatMessage> findAllMessages();

    /**
     * 특정 ID 이후의 메시지 조회 (실시간 업데이트용)
     */
    List<ChatMessage> findMessagesAfterId(@Param("lastId") Long lastId);

    /**
     * 메시지 삭제 (관리자용)
     */
    void deleteMessage(@Param("id") Long id);

    /**
     * 전체 메시지 개수 조회
     */
    int countAllMessages();
}
