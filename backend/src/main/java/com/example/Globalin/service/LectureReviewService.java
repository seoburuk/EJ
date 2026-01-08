package com.example.Globalin.service;

import com.example.Globalin.dao.LectureReviewDao;
import com.example.Globalin.model.LectureReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LectureReviewService {

    @Autowired
    private LectureReviewDao lectureReviewDao;

    /**
     * レビュー作成
     */
    @Transactional
    public LectureReview createReview(LectureReview review, Long userId) {
        // レーティングバリデーション
        if (review.getDifficulty() < 1 || review.getDifficulty() > 5) {
            throw new RuntimeException("難易度は1～5の値である必要があります");
        }
        if (review.getWorkload() < 1 || review.getWorkload() > 5) {
            throw new RuntimeException("課題量は1～5の値である必要があります");
        }
        if (review.getSatisfaction() < 1 || review.getSatisfaction() > 5) {
            throw new RuntimeException("満足度は1～5の値である必要があります");
        }

        review.setUserId(userId);
        lectureReviewDao.insertReview(review);
        return review;
    }

    /**
     * レビュー取得
     */
    public LectureReview getReview(Long id) {
        return lectureReviewDao.findById(id);
    }

    /**
     * レビュー更新
     */
    @Transactional
    public LectureReview updateReview(Long id, LectureReview review, Long userId) {
        LectureReview existingReview = lectureReviewDao.findById(id);
        if (existingReview == null) {
            throw new RuntimeException("レビューが見つかりません");
        }

        // 権限確認
        if (!existingReview.getUserId().equals(userId)) {
            throw new RuntimeException("レビューを編集する権限がありません");
        }

        // レーティングバリデーション
        if (review.getDifficulty() < 1 || review.getDifficulty() > 5) {
            throw new RuntimeException("難易度は1～5の値である必要があります");
        }
        if (review.getWorkload() < 1 || review.getWorkload() > 5) {
            throw new RuntimeException("課題量は1～5の値である必要があります");
        }
        if (review.getSatisfaction() < 1 || review.getSatisfaction() > 5) {
            throw new RuntimeException("満足度は1～5の値である必要があります");
        }

        review.setId(id);
        review.setUserId(userId);
        lectureReviewDao.updateReview(review);
        return lectureReviewDao.findById(id);
    }

    /**
     * レビュー削除
     */
    @Transactional
    public void deleteReview(Long id, Long userId) {
        LectureReview review = lectureReviewDao.findById(id);
        if (review == null) {
            throw new RuntimeException("レビューが見つかりません");
        }

        // 権限確認
        if (!review.getUserId().equals(userId)) {
            throw new RuntimeException("レビューを削除する権限がありません");
        }

        lectureReviewDao.deleteReview(id);
    }

    /**
     * 講義名/教授名で検索
     */
    public List<LectureReview> searchReviews(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return lectureReviewDao.findAllReviews(100, 0);
        }
        return lectureReviewDao.searchReviews(keyword.trim());
    }

    /**
     * 講義名で検索
     */
    public List<LectureReview> getReviewsByCourseName(String courseName) {
        return lectureReviewDao.findByCourseName(courseName);
    }

    /**
     * 教授名で検索
     */
    public List<LectureReview> getReviewsByProfessor(String professor) {
        return lectureReviewDao.findByProfessor(professor);
    }

    /**
     * 講義名+教授名で検索
     */
    public Map<String, Object> getReviewsByCourseAndProfessor(String courseName, String professor) {
        List<LectureReview> reviews = lectureReviewDao.findByCourseAndProfessor(courseName, professor);
        Map<String, Double> avgRatings = lectureReviewDao.getAverageRatings(courseName, professor);
        int count = lectureReviewDao.countReviewsByCourseAndProfessor(courseName, professor);

        Map<String, Object> result = new HashMap<>();
        result.put("reviews", reviews);
        result.put("averageRatings", avgRatings);
        result.put("count", count);
        return result;
    }

    /**
     * ユーザーが作成したレビュー一覧
     */
    public List<LectureReview> getMyReviews(Long userId) {
        return lectureReviewDao.findByUserId(userId);
    }

    /**
     * すべてのレビュー取得（ページング）
     */
    public List<LectureReview> getAllReviews(Integer limit, Integer offset) {
        return lectureReviewDao.findAllReviews(limit, offset);
    }

    /**
     * レビュー総件数
     */
    public int getReviewCount() {
        return lectureReviewDao.countReviews();
    }
}
