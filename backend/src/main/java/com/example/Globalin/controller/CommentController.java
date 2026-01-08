package com.example.Globalin.controller;

import com.example.Globalin.dto.request.CreateCommentRequest;
import com.example.Globalin.dto.request.UpdateCommentRequest;
import com.example.Globalin.dto.response.CommentResponse;
import com.example.Globalin.model.Comment;
import com.example.Globalin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * コメント作成
     */
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CreateCommentRequest request,
                                                          HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommentResponse(false, "ログインが必要です。"));
        }

        CommentResponse response = commentService.createComment(request, userId);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * 投稿の全コメント照会
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    /**
     * コメント修正
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id,
                                                          @RequestBody UpdateCommentRequest request,
                                                          HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommentResponse(false, "ログインが必要です。"));
        }

        CommentResponse response = commentService.updateComment(id, request, userId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * コメント削除
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long id,
                                                          HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommentResponse(false, "ログインが必要です。"));
        }

        CommentResponse response = commentService.deleteComment(id, userId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * コメントいいねトグル
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<CommentResponse> toggleCommentLike(@PathVariable Long id,
                                                              HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommentResponse(false, "ログインが必要です。"));
        }

        CommentResponse response = commentService.toggleCommentLike(id, userId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * コメントいいね有無確認
     */
    @GetMapping("/{id}/like/status")
    public ResponseEntity<Boolean> isCommentLiked(@PathVariable Long id,
                                                   HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(false);
        }

        boolean isLiked = commentService.isCommentLiked(id, userId);
        return ResponseEntity.ok(isLiked);
    }
}
