package com.example.Globalin.controller;

import com.example.Globalin.model.Report;
import com.example.Globalin.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 게시글 신고
     */
    @PostMapping("/post/{postId}")
    public ResponseEntity<Map<String, Object>> reportPost(
            @PathVariable Long postId,
            @RequestBody Map<String, String> request,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.ok(response);
            }

            String reason = request.get("reason");
            reportService.reportPost(userId, postId, reason);

            response.put("success", true);
            response.put("message", "신고가 접수되었습니다.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "신고 접수 중 오류가 발생했습니다.");
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 사용자 신고
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> reportUser(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            Long reporterId = (Long) session.getAttribute("userId");
            if (reporterId == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.ok(response);
            }

            String reason = request.get("reason");
            reportService.reportUser(reporterId, userId, reason);

            response.put("success", true);
            response.put("message", "신고가 접수되었습니다.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "신고 접수 중 오류가 발생했습니다.");
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 모든 신고 조회 (관리자용)
     */
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports(HttpSession session) {
        // TODO: 관리자 권한 체크
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * 대기 중인 신고 조회 (관리자용)
     */
    @GetMapping("/pending")
    public ResponseEntity<List<Report>> getPendingReports(HttpSession session) {
        // TODO: 관리자 권한 체크
        List<Report> reports = reportService.getPendingReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * 대기 중인 신고 개수 (관리자용)
     */
    @GetMapping("/pending/count")
    public ResponseEntity<Integer> getPendingReportCount(HttpSession session) {
        // TODO: 관리자 권한 체크
        int count = reportService.getPendingReportCount();
        return ResponseEntity.ok(count);
    }

    /**
     * 신고 처리 (승인)
     */
    @PutMapping("/{id}/resolve")
    public ResponseEntity<Map<String, Object>> resolveReport(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            // TODO: 관리자 권한 체크

            String adminNote = request.get("adminNote");
            reportService.resolveReport(id, adminNote);

            response.put("success", true);
            response.put("message", "신고가 처리되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "신고 처리 중 오류가 발생했습니다.");
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 신고 거부
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectReport(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            // TODO: 관리자 권한 체크

            String adminNote = request.get("adminNote");
            reportService.rejectReport(id, adminNote);

            response.put("success", true);
            response.put("message", "신고가 거부되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "신고 거부 중 오류가 발생했습니다.");
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 신고 삭제 (관리자용)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteReport(
            @PathVariable Long id,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            // TODO: 관리자 권한 체크

            reportService.deleteReport(id);

            response.put("success", true);
            response.put("message", "신고가 삭제되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "신고 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.ok(response);
        }
    }
}
