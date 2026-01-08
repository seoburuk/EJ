package com.example.Globalin.dao;

import com.example.Globalin.model.LectureReview;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LectureReviewDao {
    // 리뷰 작성
    void insertReview(LectureReview review);

    // 리뷰 조회
    LectureReview findById(Long id);

    // 리뷰 수정
    void updateReview(LectureReview review);

    // 리뷰 삭제
    void deleteReview(Long id);

    // 강의명/교수명으로 검색
    List<LectureReview> searchReviews(@Param("keyword") String keyword);

    // 강의명으로 검색
    List<LectureReview> findByCourseName(@Param("courseName") String courseName);

    // 교수명으로 검색
    List<LectureReview> findByProfessor(@Param("professor") String professor);

    // 강의명 + 교수명으로 검색
    List<LectureReview> findByCourseAndProfessor(@Param("courseName") String courseName,
                                                   @Param("professor") String professor);

    // 사용자가 작성한 리뷰 목록
    List<LectureReview> findByUserId(Long userId);

    // 강의별 평균 평점
    Map<String, Double> getAverageRatings(@Param("courseName") String courseName,
                                           @Param("professor") String professor);

    // 모든 리뷰 조회 (최신순)
    List<LectureReview> findAllReviews(@Param("limit") Integer limit,
                                        @Param("offset") Integer offset);

    // 리뷰 총 개수
    int countReviews();

    // 특정 강의의 리뷰 개수
    int countReviewsByCourseAndProfessor(@Param("courseName") String courseName,
                                          @Param("professor") String professor);
}
