package com.example.Globalin.service;

import com.example.Globalin.dao.MessageDao;
import com.example.Globalin.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    /**
     * 메시지 전송
     */
    public void sendMessage(Message message) {
        messageDao.insertMessage(message);
    }

    /**
     * 메시지 조회
     */
    public Message getMessage(Long id, Long userId) {
        Message message = messageDao.findById(id);

        if (message == null) {
            return null;
        }

        // 권한 확인: 발신자 또는 수신자만 조회 가능
        if (!message.getSenderId().equals(userId) && !message.getReceiverId().equals(userId)) {
            return null;
        }

        // 수신자가 조회하면 자동으로 읽음 처리
        if (message.getReceiverId().equals(userId) && !message.getIsRead()) {
            messageDao.markAsRead(id);
            message.setIsRead(true);
        }

        return message;
    }

    /**
     * 받은 메시지 목록
     */
    public List<Message> getReceivedMessages(Long userId, int offset, int limit) {
        return messageDao.findReceivedMessages(userId, offset, limit);
    }

    /**
     * 보낸 메시지 목록
     */
    public List<Message> getSentMessages(Long userId, int offset, int limit) {
        return messageDao.findSentMessages(userId, offset, limit);
    }

    /**
     * 받은 메시지 수
     */
    public int getReceivedMessagesCount(Long userId) {
        return messageDao.countReceivedMessages(userId);
    }

    /**
     * 보낸 메시지 수
     */
    public int getSentMessagesCount(Long userId) {
        return messageDao.countSentMessages(userId);
    }

    /**
     * 안 읽은 메시지 수
     */
    public int getUnreadMessagesCount(Long userId) {
        return messageDao.countUnreadMessages(userId);
    }

    /**
     * 메시지 삭제
     */
    public void deleteMessage(Long id, Long userId) {
        Message message = messageDao.findById(id);

        if (message == null) {
            return;
        }

        // 발신자가 삭제
        if (message.getSenderId().equals(userId)) {
            messageDao.deleteForSender(id);
        }
        // 수신자가 삭제
        else if (message.getReceiverId().equals(userId)) {
            messageDao.deleteForReceiver(id);
        }

        // 양쪽 모두 삭제했으면 실제로 삭제
        if (message.getDeletedBySender() && message.getDeletedByReceiver()) {
            messageDao.deleteMessage(id);
        }
    }
}
