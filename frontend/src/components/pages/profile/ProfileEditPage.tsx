import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { UserProfileDetail, UpdateProfileRequest } from '../../../types';
import './ProfileEditPage.scss';

const ProfileEditPage: React.FC = () => {
  const { userId } = useParams<{ userId: string }>();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [profile, setProfile] = useState<UserProfileDetail | null>(null);

  const [formData, setFormData] = useState<UpdateProfileRequest>({
    nickname: '',
    email: '',
    avatar: '',
    bio: ''
  });

  useEffect(() => {
    fetchProfile();
  }, [userId]);

  const fetchProfile = async () => {
    setLoading(true);
    setError('');

    try {
      const apiUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080';
      const response = await fetch(`${apiUrl}/api/users/${userId}/profile`);

      if (!response.ok) {
        throw new Error('プロフィールを読み込めません。');
      }

      const data = await response.json();
      setProfile(data);

      // フォームの初期値設定
      setFormData({
        nickname: data.nickname || '',
        email: data.email || '',
        avatar: data.avatar || '',
        bio: data.bio || ''
      });
    } catch (err) {
      console.error('プロフィール取得エラー:', err);
      setError('プロフィールの読み込み中にエラーが発生しました。');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // バリデーション
    if (!formData.nickname?.trim()) {
      setError('ニックネームを入力してください。');
      return;
    }

    if (!formData.email?.trim()) {
      setError('メールアドレスを入力してください。');
      return;
    }

    // メールアドレス形式チェック
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      setError('正しいメールアドレス形式を入力してください。');
      return;
    }

    setSaving(true);
    setError('');

    try {
      const apiUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080';
      const response = await fetch(`${apiUrl}/api/users/${userId}/profile`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'プロフィールの更新に失敗しました。');
      }

      // 成功したらプロフィールページに移動
      navigate(`/profile/${userId}`);
    } catch (err: any) {
      console.error('プロフィール更新エラー:', err);
      setError(err.message || 'プロフィールの更新中にエラーが発生しました。');
    } finally {
      setSaving(false);
    }
  };

  const handleCancel = () => {
    navigate(`/profile/${userId}`);
  };

  if (loading) {
    return <div className="profile-edit-page"><div className="loading">読み込み中...</div></div>;
  }

  if (!profile) {
    return (
      <div className="profile-edit-page">
        <div className="error">プロフィールが見つかりません。</div>
      </div>
    );
  }

  // 本人確認（localStorageのuserIdと比較）
  const currentUserId = localStorage.getItem('userId');
  if (currentUserId !== userId) {
    return (
      <div className="profile-edit-page">
        <div className="error">自分のプロフィールのみ編集できます。</div>
      </div>
    );
  }

  return (
    <div className="profile-edit-page">
      <div className="edit-container">
        <h1>プロフィール編集</h1>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} className="edit-form">
          <div className="form-group">
            <label htmlFor="nickname">ニックネーム</label>
            <input
              type="text"
              id="nickname"
              name="nickname"
              value={formData.nickname}
              onChange={handleInputChange}
              placeholder="ニックネームを入力してください"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">メールアドレス</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              placeholder="メールアドレスを入力してください"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="avatar">アバターURL</label>
            <input
              type="url"
              id="avatar"
              name="avatar"
              value={formData.avatar}
              onChange={handleInputChange}
              placeholder="アバター画像URL（任意）"
            />
            {formData.avatar && (
              <div className="avatar-preview">
                <img src={formData.avatar} alt="プレビュー" />
              </div>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="bio">自己紹介</label>
            <textarea
              id="bio"
              name="bio"
              value={formData.bio}
              onChange={handleInputChange}
              placeholder="自己紹介を入力してください（任意）"
              rows={5}
              maxLength={500}
            />
            <div className="char-count">
              {formData.bio?.length || 0} / 500
            </div>
          </div>

          <div className="form-actions">
            <button type="submit" className="btn-save" disabled={saving}>
              {saving ? '保存中...' : '保存'}
            </button>
            <button type="button" className="btn-cancel" onClick={handleCancel} disabled={saving}>
              キャンセル
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ProfileEditPage;
