package com.example.Globalin.dao;

import com.example.Globalin.model.PostImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostImageDao {

    // 이미지 삽입
    void insertImage(PostImage postImage);

    // 게시글의 모든 이미지 조회
    List<PostImage> findImagesByPostId(@Param("postId") Long postId);

    // 이미지 삭제
    void deleteImage(@Param("id") Long id);

    // 게시글의 모든 이미지 삭제
    void deleteImagesByPostId(@Param("postId") Long postId);

    // 이미지 개수 조회
    int countImagesByPostId(@Param("postId") Long postId);
}
