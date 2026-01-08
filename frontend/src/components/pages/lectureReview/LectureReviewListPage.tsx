import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { lectureReviewApi, LectureReview } from '../../../api/lectureReview';
import './LectureReviewListPage.scss';

const LectureReviewListPage: React.FC = () => {
  const navigate = useNavigate();
  const [reviews, setReviews] = useState<LectureReview[]>([]);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadReviews();
  }, []);

  const loadReviews = async () => {
    setLoading(true);
    try {
      const data = await lectureReviewApi.searchReviews();
      setReviews(data);
    } catch (err: any) {
      setError(err.message || 'レビューリストの取得に失敗しました');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!searchKeyword.trim()) {
      loadReviews();
      return;
    }

    setLoading(true);
    try {
      const data = await lectureReviewApi.searchReviews(searchKeyword.trim());
      setReviews(data);
      setError('');
    } catch (err: any) {
      setError(err.message || '検索に失敗しました');
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

  const handleReviewClick = (courseName: string, professor: string) => {
    navigate(`/lecture-reviews/${encodeURIComponent(courseName)}/${encodeURIComponent(professor)}`);
  };

  if (loading) return <div className="loading">読み込み中...</div>;

  return (
    <div className="lecture-review-page">
      <div className="review-container">
        <div className="review-header">
          <h1>講義レビュー</h1>
          <button className="write-review-btn" onClick={() => navigate('/lecture-reviews/write')}>
            + レビュー作成
          </button>
        </div>

        <form className="search-section" onSubmit={handleSearch}>
          <input
            type="text"
            placeholder="講義名または教授名を検索してください..."
            value={searchKeyword}
            onChange={(e) => setSearchKeyword(e.target.value)}
            className="search-input"
          />
          <button type="submit" className="search-btn">検索</button>
        </form>

        {error && <div className="error-message">{error}</div>}

        <div className="review-list">
          {reviews.length === 0 ? (
            <div className="empty-message">
              登録された講義レビューがありません。
            </div>
          ) : (
            reviews.map((review) => (
              <div
                key={review.id}
                className="review-card"
                onClick={() => handleReviewClick(review.courseName, review.professor)}
              >
                <div className="review-card-header">
                  <div className="course-info">
                    <h3 className="course-name">{review.courseName}</h3>
                    <p className="professor">{review.professor} 教授</p>
                  </div>
                  <div className="semester-info">
                    {review.year && review.semester && (
                      <span>{review.year}年 {review.semester}</span>
                    )}
                  </div>
                </div>

                <div className="rating-summary">
                  <div className="rating-item">
                    <span className="label">難易度</span>
                    {renderStars(review.difficulty)}
                    <span className="value">{review.difficulty.toFixed(1)}</span>
                  </div>
                  <div className="rating-item">
                    <span className="label">課題量</span>
                    {renderStars(review.workload)}
                    <span className="value">{review.workload.toFixed(1)}</span>
                  </div>
                  <div className="rating-item">
                    <span className="label">満足度</span>
                    {renderStars(review.satisfaction)}
                    <span className="value">{review.satisfaction.toFixed(1)}</span>
                  </div>
                </div>

                {review.reviewText && (
                  <div className="review-preview">
                    {review.reviewText.length > 100
                      ? review.reviewText.substring(0, 100) + '...'
                      : review.reviewText}
                  </div>
                )}

                <div className="review-footer">
                  <span className="author">{review.authorNickname}</span>
                  <span className="date">
                    {review.createdAt && new Date(review.createdAt).toLocaleDateString('ko-KR')}
                  </span>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default LectureReviewListPage;
