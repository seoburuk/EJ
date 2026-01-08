package com.example.Globalin.service;

import com.example.Globalin.dao.CommentDao;
import com.example.Globalin.dao.PostDao;
import com.example.Globalin.dao.UserDao;
import com.example.Globalin.dto.request.CreateCommentRequest;
import com.example.Globalin.dto.request.UpdateCommentRequest;
import com.example.Globalin.dto.response.CommentResponse;
import com.example.Globalin.model.Comment;
import com.example.Globalin.model.Post;
import com.example.Globalin.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NotificationService notificationService;

    /**
     * コメント作成
     */
    @Transactional
    public CommentResponse createComment(CreateCommentRequest request, Long userId) {
        // バリデーション
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return new CommentResponse(false, "コメント内容を入力してください");
        }

        Comment comment = new Comment();
        comment.setPostId(request.getPostId());
        comment.setAuthorId(userId);
        comment.setParentId(request.getParentId());
        comment.setContent(request.getContent());
        comment.setIsAnonymous(request.getIsAnonymous() != null ? request.getIsAnonymous() : false);

        commentDao.insertComment(comment);

        // 通知作成
        User commenter = userDao.findById(userId);
        String commenterName = commenter.getNickname() != null ? commenter.getNickname() : commenter.getUsername();

        if (request.getParentId() != null) {
            // 返信の場合：親コメント作成者に通知
            Comment parentComment = commentDao.findById(request.getParentId());
            if (parentComment != null) {
                String message = String.format("%sさんがあなたのコメントに返信しました: %s",
                    commenterName,
                    comment.getContent().length() > 30 ? comment.getContent().substring(0, 30) + "..." : comment.getContent());

                notificationService.createNotification(
                    parentComment.getAuthorId(),
                    "REPLY",
                    comment.getId(),
                    userId,
                    commenterName,
                    message
                );
            }
        } else {
            // 通常のコメントの場合：投稿作成者に通知
            Post post = postDao.findById(request.getPostId());
            if (post != null) {
                String message = String.format("%sさんがあなたの投稿「%s」にコメントしました: %s",
                    commenterName,
                    post.getTitle().length() > 20 ? post.getTitle().substring(0, 20) + "..." : post.getTitle(),
                    comment.getContent().length() > 30 ? comment.getContent().substring(0, 30) + "..." : comment.getContent());

                notificationService.createNotification(
                    post.getAuthorId(),
                    "COMMENT",
                    comment.getId(),
                    userId,
                    commenterName,
                    message
                );
            }
        }

        return new CommentResponse(true, "コメントが作成されました", comment.getId());
    }

    /**
     * コメント取得
     */
    public Comment getCommentById(Long id) {
        return commentDao.findById(id);
    }

    /**
     * 投稿のすべてのコメント取得
     */
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentDao.findByPostId(postId);
    }

    /**
     * コメント更新
     */
    @Transactional
    public CommentResponse updateComment(Long commentId, UpdateCommentRequest request, Long userId) {
        Comment comment = commentDao.findById(commentId);

        if (comment == null) {
            return new CommentResponse(false, "コメントが見つかりません");
        }

        // 作成者確認
        if (!comment.getAuthorId().equals(userId)) {
            return new CommentResponse(false, "コメントを編集する権限がありません");
        }

        // バリデーション
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return new CommentResponse(false, "コメント内容を入力してください");
        }

        comment.setContent(request.getContent());
        commentDao.updateComment(comment);

        return new CommentResponse(true, "コメントが更新されました", commentId);
    }

    /**
     * コメント削除
     */
    @Transactional
    public CommentResponse deleteComment(Long commentId, Long userId) {
        Comment comment = commentDao.findById(commentId);

        if (comment == null) {
            return new CommentResponse(false, "コメントが見つかりません");
        }

        // 作成者確認
        if (!comment.getAuthorId().equals(userId)) {
            return new CommentResponse(false, "コメントを削除する権限がありません");
        }

        commentDao.deleteComment(commentId);

        return new CommentResponse(true, "コメントが削除されました");
    }

    /**
     * 投稿のコメント数取得
     */
    public int getCommentCount(Long postId) {
        return commentDao.countByPostId(postId);
    }

    /**
     * コメント「いいね」トグル
     */
    @Transactional
    public CommentResponse toggleCommentLike(Long commentId, Long userId) {
        Comment comment = commentDao.findById(commentId);

        if (comment == null) {
            return new CommentResponse(false, "コメントが見つかりません");
        }

        boolean isLiked = commentDao.existsCommentLike(commentId, userId);

        if (isLiked) {
            // 「いいね」キャンセル
            commentDao.deleteCommentLike(commentId, userId);
            commentDao.updateCommentLikeCount(commentId);
            return new CommentResponse(true, "いいねがキャンセルされました", false);
        } else {
            // 「いいね」追加
            commentDao.insertCommentLike(commentId, userId);
            commentDao.updateCommentLikeCount(commentId);
            return new CommentResponse(true, "いいねが追加されました", true);
        }
    }

    /**
     * コメントに「いいね」が押されたかどうか確認
     */
    public boolean isCommentLiked(Long commentId, Long userId) {
        return commentDao.existsCommentLike(commentId, userId);
    }

    /**
     * コメント「いいね」数取得
     */
    public int getCommentLikeCount(Long commentId) {
        return commentDao.getCommentLikeCount(commentId);
    }
}
