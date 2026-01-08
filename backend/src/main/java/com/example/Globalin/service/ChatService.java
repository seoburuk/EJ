package com.example.Globalin.service;

import com.example.Globalin.dao.ChatMessageDao;
import com.example.Globalin.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * チャットサービス
 * チャットメッセージの保存、取得などのビジネスロジック処理
 */
@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private ChatMessageDao chatMessageDao;

    /**
     * チャットメッセージ保存
     */
    @Transactional
    public ChatMessage saveMessage(String username, String message) {
        ChatMessage chatMessage = new ChatMessage(username, message);

        try {
            chatMessageDao.insertMessage(chatMessage);
            logger.info("チャットメッセージ保存成功 - ユーザー: {}, メッセージID: {}", username, chatMessage.getId());
            return chatMessage;
        } catch (Exception e) {
            logger.error("チャットメッセージ保存失敗 - ユーザー: {}, エラー: {}", username, e.getMessage());
            throw new RuntimeException("メッセージ保存中にエラーが発生しました", e);
        }
    }

    /**
     * 最近のメッセージ取得（デフォルト100件）
     */
    public List<ChatMessage> getRecentMessages() {
        return getRecentMessages(100);
    }

    /**
     * 最近のメッセージ取得（件数指定）
     */
    public List<ChatMessage> getRecentMessages(int limit) {
        try {
            return chatMessageDao.findRecentMessages(limit);
        } catch (Exception e) {
            logger.error("最近のメッセージ取得失敗: {}", e.getMessage());
            throw new RuntimeException("メッセージ取得中にエラーが発生しました", e);
        }
    }

    /**
     * すべてのメッセージ取得
     */
    public List<ChatMessage> getAllMessages() {
        try {
            return chatMessageDao.findAllMessages();
        } catch (Exception e) {
            logger.error("全メッセージ取得失敗: {}", e.getMessage());
            throw new RuntimeException("メッセージ取得中にエラーが発生しました", e);
        }
    }

    /**
     * 特定IDの後のメッセージ取得（リアルタイム更新用）
     */
    public List<ChatMessage> getMessagesAfterId(Long lastId) {
        try {
            return chatMessageDao.findMessagesAfterId(lastId);
        } catch (Exception e) {
            logger.error("メッセージ取得失敗（ID以後）: {}", e.getMessage());
            throw new RuntimeException("メッセージ取得中にエラーが発生しました", e);
        }
    }

    /**
     * メッセージ削除（管理者用）
     */
    @Transactional
    public void deleteMessage(Long id) {
        try {
            chatMessageDao.deleteMessage(id);
            logger.info("チャットメッセージ削除成功 - ID: {}", id);
        } catch (Exception e) {
            logger.error("メッセージ削除失敗 - ID: {}, エラー: {}", id, e.getMessage());
            throw new RuntimeException("メッセージ削除中にエラーが発生しました", e);
        }
    }

    /**
     * 全メッセージ件数取得
     */
    public int getTotalMessageCount() {
        try {
            return chatMessageDao.countAllMessages();
        } catch (Exception e) {
            logger.error("メッセージ件数取得失敗: {}", e.getMessage());
            return 0;
        }
    }
}
