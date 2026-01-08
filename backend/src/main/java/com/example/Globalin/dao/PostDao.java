package com.example.Globalin.dao;

import com.example.Globalin.model.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostDao {

    // 게시글 생성
    void insertPost(Post post);

    // 게시글 조회 (ID)
    Post findById(@Param("id") Long id);

    // 게시판별 게시글 목록 조회
    List<Post> findByBoardId(@Param("boardId") Long boardId,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    // 전체 게시글 목록 조회
    List<Post> findAll(@Param("offset") int offset,
                       @Param("limit") int limit);

    // 사용자별 게시글 목록 조회
    List<Post> findByAuthorId(@Param("authorId") Long authorId,
                              @Param("offset") int offset,
                              @Param("limit") int limit);

    // 게시글 업데이트
    void updatePost(Post post);

    // 게시글 삭제
    void deletePost(@Param("id") Long id);

    // 조회수 증가
    void incrementViewCount(@Param("id") Long id);

    // 좋아요 수 증가/감소
    void incrementLikeCount(@Param("id") Long id);
    void decrementLikeCount(@Param("id") Long id);

    // 댓글 수 증가/감소
    void incrementCommentCount(@Param("id") Long id);
    void decrementCommentCount(@Param("id") Long id);

    // 이미지 수 업데이트
    void updateImageCount(@Param("id") Long id, @Param("count") int count);

    // 게시판별 게시글 수
    int countByBoardId(@Param("boardId") Long boardId);

    // 검색
    List<Post> searchPosts(@Param("keyword") String keyword,
                          @Param("offset") int offset,
                          @Param("limit") int limit);

    // 전체 게시글 수 조회
    int countAllPosts();
}
