package com.example.Globalin.controller;

import com.example.Globalin.dto.request.ChatMessageDTO;
import com.example.Globalin.model.ChatMessage;
import com.example.Globalin.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * チャットコントローラー
 * WebSocketを通じたリアルタイムチャットメッセージ処理
 */
@Controller
@RequestMapping("/api/chat")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    /**
     * チャットメッセージ受信およびブロードキャスト
     * クライアントが /app/chat.sendMessage でメッセージを送信すると
     * /topic/messages を購読中のすべてのクライアントにブロードキャスト
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessageDTO messageDTO, SimpMessageHeaderAccessor headerAccessor) {
        logger.info("チャットメッセージ受信 - ユーザー: {}, メッセージ: {}", messageDTO.getUsername(), messageDTO.getMessage());

        try {
            // データベースにメッセージを保存
            ChatMessage savedMessage = chatService.saveMessage(
                    messageDTO.getUsername(),
                    messageDTO.getMessage()
            );

            logger.info("チャットメッセージブロードキャスト - ID: {}", savedMessage.getId());
            return savedMessage;

        } catch (Exception e) {
            logger.error("チャットメッセージ処理中のエラー: {}", e.getMessage());
            // エラー発生時もメッセージをブロードキャスト (保存失敗通知)
            ChatMessage errorMessage = new ChatMessage();
            errorMessage.setUsername("System");
            errorMessage.setMessage("メッセージ送信中にエラーが発生しました。");
            return errorMessage;
        }
    }

    /**
     * ユーザー入場通知
     * クライアントが /app/chat.addUser で入場メッセージを送信すると
     * /topic/messages を購読中のすべてのクライアントにブロードキャスト
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/messages")
    public ChatMessage addUser(ChatMessageDTO messageDTO, SimpMessageHeaderAccessor headerAccessor) {
        // WebSocketセッションにユーザー名を保存
        headerAccessor.getSessionAttributes().put("username", messageDTO.getUsername());

        logger.info("ユーザー入場 - {}", messageDTO.getUsername());

        // 入場メッセージ生成 (データベースには保存しない)
        ChatMessage joinMessage = new ChatMessage();
        joinMessage.setUsername("System");
        joinMessage.setMessage(messageDTO.getUsername() + "さんが入場しました。");

        return joinMessage;
    }

    /**
     * REST API: 最近のチャットメッセージ照会
     */
    @GetMapping("/recent")
    @ResponseBody
    public List<ChatMessage> getRecentMessages(@RequestParam(defaultValue = "50") int limit) {
        logger.info("最近のチャットメッセージ照会リクエスト - limit: {}", limit);
        return chatService.getRecentMessages(limit);
    }

    /**
     * REST API: 全体メッセージ数の照会
     */
    @GetMapping("/count")
    @ResponseBody
    public int getTotalCount() {
        return chatService.getTotalMessageCount();
    }
}
