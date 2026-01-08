package com.example.Globalin.dao;

import com.example.Globalin.model.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostLikeDao {

    // 좋아요 추가
    void insertLike(PostLike postLike);

    // 좋아요 취소
    void deleteLike(@Param("postId") Long postId, @Param("userId") Long userId);

    // 특정 게시글의 좋아요 수 조회
    int countByPostId(@Param("postId") Long postId);

    // 사용자가 특정 게시글에 좋아요를 눌렀는지 확인
    int existsByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    // 사용자가 좋아요한 게시글 ID 목록 조회
    java.util.List<Long> findLikedPostIdsByUserId(@Param("userId") Long userId);
}
