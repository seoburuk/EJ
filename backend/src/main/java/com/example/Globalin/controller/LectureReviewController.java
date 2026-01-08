package com.example.Globalin.controller;

import com.example.Globalin.model.LectureReview;
import com.example.Globalin.service.LectureReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lecture-reviews")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LectureReviewController {

    @Autowired
    private LectureReviewService lectureReviewService;

    /**
     * 리뷰 작성
     */
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody LectureReview review, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            LectureReview createdReview = lectureReviewService.createReview(review, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "리뷰가 작성되었습니다.");
            response.put("review", createdReview);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 리뷰 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getReview(@PathVariable Long id) {
        try {
            LectureReview review = lectureReviewService.getReview(id);
            if (review == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "리뷰를 찾을 수 없습니다.");
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id,
                                         @RequestBody LectureReview review,
                                         HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            LectureReview updatedReview = lectureReviewService.updateReview(id, review, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "리뷰가 수정되었습니다.");
            response.put("review", updatedReview);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            lectureReviewService.deleteReview(id, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "리뷰가 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 강의명/교수명으로 검색
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchReviews(@RequestParam(required = false) String keyword) {
        try {
            List<LectureReview> reviews = lectureReviewService.searchReviews(keyword);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 강의명 + 교수명으로 상세 검색
     */
    @GetMapping("/course")
    public ResponseEntity<?> getReviewsByCourse(
            @RequestParam String courseName,
            @RequestParam String professor) {
        try {
            Map<String, Object> result = lectureReviewService.getReviewsByCourseAndProfessor(
                    courseName, professor);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 내가 작성한 리뷰 목록
     */
    @GetMapping("/my-reviews")
    public ResponseEntity<?> getMyReviews(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            List<LectureReview> reviews = lectureReviewService.getMyReviews(userId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 모든 리뷰 조회 (페이징)
     */
    @GetMapping
    public ResponseEntity<?> getAllReviews(
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset) {
        try {
            List<LectureReview> reviews = lectureReviewService.getAllReviews(limit, offset);
            int totalCount = lectureReviewService.getReviewCount();

            Map<String, Object> response = new HashMap<>();
            response.put("reviews", reviews);
            response.put("totalCount", totalCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
