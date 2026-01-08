package com.example.Globalin.dao;

import com.example.Globalin.model.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportDao {

    // 게시글 신고
    void insertPostReport(@Param("reporterId") Long reporterId,
                         @Param("postId") Long postId,
                         @Param("reason") String reason);

    // 사용자 신고
    void insertUserReport(@Param("reporterId") Long reporterId,
                         @Param("userId") Long userId,
                         @Param("reason") String reason);

    // 신고 조회
    Report findById(@Param("id") Long id);

    // 모든 신고 조회 (관리자용)
    List<Report> findAll();

    // 대기 중인 신고 조회
    List<Report> findPendingReports();

    // 신고 상태 업데이트
    void updateStatus(@Param("id") Long id,
                     @Param("status") String status,
                     @Param("adminNote") String adminNote);

    // 신고 삭제
    void deleteReport(@Param("id") Long id);

    // 대기 중인 신고 개수
    int countPendingReports();
}
