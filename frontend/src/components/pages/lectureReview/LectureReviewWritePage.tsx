import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { lectureReviewApi, LectureReview } from '../../../api/lectureReview';
import './LectureReviewWritePage.scss';

const LectureReviewWritePage: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<LectureReview>({
    courseName: '',
    professor: '',
    difficulty: 3,
    workload: 3,
    satisfaction: 3,
    reviewText: undefined,
    semester: undefined,
    year: new Date().getFullYear(),
    isAnonymous: false,
  });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.courseName.trim()) {
      setError('講義名を入力してください。');
      return;
    }
    if (!formData.professor.trim()) {
      setError('教授名を入力してください。');
      return;
    }

    setSubmitting(true);
    setError('');

    try {
      // 空文字列をundefinedに変換
      const submitData: LectureReview = {
        ...formData,
        semester: formData.semester?.trim() || undefined,
        reviewText: formData.reviewText?.trim() || undefined,
      };
      await lectureReviewApi.createReview(submitData);
      alert('講義レビューが作成されました。');
      navigate('/lecture-reviews');
    } catch (err: any) {
      setError(err.message || '講義レビューの作成に失敗しました。');
    } finally {
      setSubmitting(false);
    }
  };

  const renderStarInput = (label: string, value: number, onChange: (rating: number) => void) => {
    return (
      <div className="star-input">
        <label>{label}</label>
        <div className="stars-container">
          {[1, 2, 3, 4, 5].map((star) => (
            <span
              key={star}
              className={star <= value ? 'star filled' : 'star'}
              onClick={() => onChange(star)}
            >
              ★
            </span>
          ))}
          <span className="rating-value">{value}</span>
        </div>
      </div>
    );
  };

  return (
    <div className="review-write-page">
      <div className="write-container">
        <div className="write-header">
          <h1>講義レビュー作成</h1>
          <button className="back-btn" onClick={() => navigate('/lecture-reviews')}>
            キャンセル
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} className="write-form">
          <div className="form-section">
            <h3>講義情報</h3>
            <div className="form-group">
              <label>講義名 *</label>
              <input
                type="text"
                value={formData.courseName}
                onChange={(e) => setFormData({ ...formData, courseName: e.target.value })}
                placeholder="例：Javaプログラミング"
                required
              />
            </div>

            <div className="form-group">
              <label>教授 *</label>
              <input
                type="text"
                value={formData.professor}
                onChange={(e) => setFormData({ ...formData, professor: e.target.value })}
                placeholder="例：金教授"
                required
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>受講年度</label>
                <input
                  type="number"
                  value={formData.year}
                  onChange={(e) => setFormData({ ...formData, year: parseInt(e.target.value) })}
                  min="2000"
                  max="2099"
                />
              </div>

              <div className="form-group">
                <label>学期</label>
                <select
                  value={formData.semester || ''}
                  onChange={(e) => setFormData({ ...formData, semester: e.target.value || undefined })}
                >
                  <option value="">選択</option>
                  <option value="1学期">1学期</option>
                  <option value="2学期">2学期</option>
                  <option value="夏学期">夏学期</option>
                  <option value="冬学期">冬学期</option>
                </select>
              </div>
            </div>
          </div>

          <div className="form-section">
            <h3>評価（1-5点）</h3>
            {renderStarInput('難易度', formData.difficulty, (rating) =>
              setFormData({ ...formData, difficulty: rating })
            )}
            {renderStarInput('課題量', formData.workload, (rating) =>
              setFormData({ ...formData, workload: rating })
            )}
            {renderStarInput('満足度', formData.satisfaction, (rating) =>
              setFormData({ ...formData, satisfaction: rating })
            )}
          </div>

          <div className="form-section">
            <h3>レビュー</h3>
            <div className="form-group">
              <label>詳細レビュー（任意）</label>
              <textarea
                value={formData.reviewText || ''}
                onChange={(e) => setFormData({ ...formData, reviewText: e.target.value || undefined })}
                placeholder="講義に関する詳しいレビューを作成してください..."
                rows={6}
              />
            </div>

            <div className="form-group checkbox">
              <label>
                <input
                  type="checkbox"
                  checked={formData.isAnonymous}
                  onChange={(e) => setFormData({ ...formData, isAnonymous: e.target.checked })}
                />
                匿名で作成
              </label>
            </div>
          </div>

          <div className="form-actions">
            <button type="button" className="cancel-btn" onClick={() => navigate('/lecture-reviews')}>
              キャンセル
            </button>
            <button type="submit" className="submit-btn" disabled={submitting}>
              {submitting ? '作成中...' : '作成完了'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default LectureReviewWritePage;
