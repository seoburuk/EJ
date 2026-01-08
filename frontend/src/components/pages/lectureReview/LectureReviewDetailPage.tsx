import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { lectureReviewApi, ReviewSearchResult } from '../../../api/lectureReview';
import './LectureReviewDetailPage.scss';

const LectureReviewDetailPage: React.FC = () => {
  const { courseName, professor } = useParams<{ courseName: string; professor: string }>();
  const navigate = useNavigate();
  const [data, setData] = useState<ReviewSearchResult | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (courseName && professor) {
      loadReviews();
    }
  }, [courseName, professor]);

  const loadReviews = async () => {
    if (!courseName || !professor) return;

    setLoading(true);
    try {
      const result = await lectureReviewApi.getReviewsByCourse(
        decodeURIComponent(courseName),
        decodeURIComponent(professor)
      );
      setData(result);
    } catch (err: any) {
      setError(err.message || '講義情報の取得に失敗しました');
    } finally {
      setLoading(false);
    }
  };

  const renderStars = (rating: number) => {
    return (
      <div className="stars">
        {[1, 2, 3, 4, 5].map((star) => (
          <span key={star} className={star <= rating ? 'star filled' : 'star'}>
            ★
          </span>
        ))}
      </div>
    );
  };

  if (loading) return <div className="loading">読み込み中...</div>;
  if (error) return <div className="error-message">{error}</div>;
  if (!data) return <div className="error-message">講義情報が見つかりません。</div>;

  const avgRatings = data.averageRatings || { avgDifficulty: 0, avgWorkload: 0, avgSatisfaction: 0 };

  return (
    <div className="lecture-detail-page">
      <div className="detail-container">
        <button className="back-btn" onClick={() => navigate('/lecture-reviews')}>
          ← リストに戻る
        </button>

        <div className="lecture-header">
          <h1>{decodeURIComponent(courseName!)}</h1>
          <h2>{decodeURIComponent(professor!)} 教授</h2>
        </div>

        <div className="average-ratings">
          <h3>平均評価 ({data.count}件のレビュー)</h3>
          <div className="rating-grid">
            <div className="rating-box">
              <div className="rating-label">難易度</div>
              {renderStars(Math.round(avgRatings.avgDifficulty || 0))}
              <div className="rating-value">{(avgRatings.avgDifficulty || 0).toFixed(1)}</div>
            </div>
            <div className="rating-box">
              <div className="rating-label">課題量</div>
              {renderStars(Math.round(avgRatings.avgWorkload || 0))}
              <div className="rating-value">{(avgRatings.avgWorkload || 0).toFixed(1)}</div>
            </div>
            <div className="rating-box">
              <div className="rating-label">満足度</div>
              {renderStars(Math.round(avgRatings.avgSatisfaction || 0))}
              <div className="rating-value">{(avgRatings.avgSatisfaction || 0).toFixed(1)}</div>
            </div>
          </div>
        </div>

        <div className="reviews-section">
          <h3>レビュー ({data.reviews.length}件)</h3>
          {data.reviews.length === 0 ? (
            <div className="empty-message">まだレビューがありません。</div>
          ) : (
            <div className="reviews-list">
              {data.reviews.map((review) => (
                <div key={review.id} className="review-item">
                  <div className="review-header">
                    <div className="review-author">
                      <span className="nickname">{review.authorNickname}</span>
                      {review.year && review.semester && (
                        <span className="semester">{review.year}年 {review.semester}</span>
                      )}
                    </div>
                    <div className="review-date">
                      {review.createdAt && new Date(review.createdAt).toLocaleDateString('ko-KR')}
                    </div>
                  </div>

                  <div className="review-ratings">
                    <div className="rating-badge">
                      <span className="badge-label">難易度</span>
                      <span className="badge-value">{review.difficulty}</span>
                    </div>
                    <div className="rating-badge">
                      <span className="badge-label">課題量</span>
                      <span className="badge-value">{review.workload}</span>
                    </div>
                    <div className="rating-badge">
                      <span className="badge-label">満足度</span>
                      <span className="badge-value">{review.satisfaction}</span>
                    </div>
                  </div>

                  {review.reviewText && (
                    <div className="review-text">{review.reviewText}</div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default LectureReviewDetailPage;
