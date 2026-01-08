package com.example.Globalin.service;

import com.example.Globalin.dao.ReportDao;
import com.example.Globalin.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportDao reportDao;

    /**
     * 投稿報告
     */
    @Transactional
    public void reportPost(Long reporterId, Long postId, String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("報告理由を入力してください");
        }

        reportDao.insertPostReport(reporterId, postId, reason.trim());
    }

    /**
     * ユーザー報告
     */
    @Transactional
    public void reportUser(Long reporterId, Long userId, String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("報告理由を入力してください");
        }

        reportDao.insertUserReport(reporterId, userId, reason.trim());
    }

    /**
     * 報告取得
     */
    public Report getReport(Long id) {
        Report report = reportDao.findById(id);
        if (report == null) {
            throw new RuntimeException("報告が見つかりません");
        }
        return report;
    }

    /**
     * すべての報告取得（管理者用）
     */
    public List<Report> getAllReports() {
        return reportDao.findAll();
    }

    /**
     * 待機中の報告取得
     */
    public List<Report> getPendingReports() {
        return reportDao.findPendingReports();
    }

    /**
     * 報告処理（承認）
     */
    @Transactional
    public void resolveReport(Long id, String adminNote) {
        reportDao.updateStatus(id, "resolved", adminNote);
    }

    /**
     * 報告却下
     */
    @Transactional
    public void rejectReport(Long id, String adminNote) {
        reportDao.updateStatus(id, "rejected", adminNote);
    }

    /**
     * 報告削除
     */
    @Transactional
    public void deleteReport(Long id) {
        reportDao.deleteReport(id);
    }

    /**
     * 待機中の報告件数
     */
    public int getPendingReportCount() {
        return reportDao.countPendingReports();
    }
}
