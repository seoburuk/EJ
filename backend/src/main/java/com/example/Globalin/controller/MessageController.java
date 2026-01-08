package com.example.Globalin.controller;

import com.example.Globalin.model.Message;
import com.example.Globalin.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 로그인 확인
     */
    private Long getUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }

    /**
     * 메시지 전송
     */
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> request, HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        try {
            Message message = new Message();
            message.setSenderId(userId);
            message.setReceiverId(Long.valueOf(request.get("receiverId").toString()));
            message.setTitle((String) request.get("title"));
            message.setContent((String) request.get("content"));

            messageService.sendMessage(message);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "메시지가 전송되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "메시지 전송에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * 받은 메시지 목록
     */
    @GetMapping("/received")
    public ResponseEntity<?> getReceivedMessages(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit,
            HttpSession session) {

        Long userId = getUserId(session);
        if (userId == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        List<Message> messages = messageService.getReceivedMessages(userId, offset, limit);
        int total = messageService.getReceivedMessagesCount(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("messages", messages);
        response.put("total", total);

        return ResponseEntity.ok(response);
    }

    /**
     * 보낸 메시지 목록
     */
    @GetMapping("/sent")
    public ResponseEntity<?> getSentMessages(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit,
            HttpSession session) {

        Long userId = getUserId(session);
        if (userId == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        List<Message> messages = messageService.getSentMessages(userId, offset, limit);
        int total = messageService.getSentMessagesCount(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("messages", messages);
        response.put("total", total);

        return ResponseEntity.ok(response);
    }

    /**
     * 메시지 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMessage(@PathVariable Long id, HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        Message message = messageService.getMessage(id, userId);
        if (message == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "메시지를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    /**
     * 안 읽은 메시지 수
     */
    @GetMapping("/unread/count")
    public ResponseEntity<?> getUnreadCount(HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        int count = messageService.getUnreadMessagesCount(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", count);

        return ResponseEntity.ok(response);
    }

    /**
     * 메시지 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id, HttpSession session) {
        Long userId = getUserId(session);
        if (userId == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        messageService.deleteMessage(id, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "메시지가 삭제되었습니다.");

        return ResponseEntity.ok(response);
    }
}
