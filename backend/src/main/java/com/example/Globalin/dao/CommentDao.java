package com.example.Globalin.dao;

import com.example.Globalin.model.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentDao {
    // 댓글 작성
    void insertComment(Comment comment);

    // 댓글 조회
    Comment findById(Long id);

    // 게시글의 모든 댓글 조회
    List<Comment> findByPostId(Long postId);

    // 특정 댓글의 대댓글 조회
    List<Comment> findByParentId(Long parentId);

    // 댓글 수정
    void updateComment(Comment comment);

    // 댓글 삭제 (소프트 삭제)
    void deleteComment(Long id);

    // 게시글의 댓글 수 조회
    int countByPostId(Long postId);

    // 사용자별 댓글 목록 조회
    List<Comment> findByAuthorId(@Param("authorId") Long authorId,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    // 사용자별 댓글 수 조회
    int countByAuthorId(@Param("authorId") Long authorId);

    // 댓글 좋아요 추가
    void insertCommentLike(@Param("commentId") Long commentId, @Param("userId") Long userId);

    // 댓글 좋아요 삭제
    void deleteCommentLike(@Param("commentId") Long commentId, @Param("userId") Long userId);

    // 댓글 좋아요 여부 확인
    boolean existsCommentLike(@Param("commentId") Long commentId, @Param("userId") Long userId);

    // 댓글 좋아요 수 조회
    int getCommentLikeCount(@Param("commentId") Long commentId);

    // 댓글 좋아요 수 업데이트
    void updateCommentLikeCount(@Param("commentId") Long commentId);

    // 전체 댓글 수 조회
    int countAllComments();
}
