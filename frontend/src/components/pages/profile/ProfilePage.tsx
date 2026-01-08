import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { UserProfileDetail } from '../../../types';
import './ProfilePage.scss';

const ProfilePage: React.FC = () => {
  const { userId } = useParams<{ userId: string }>();
  const navigate = useNavigate();
  const [profile, setProfile] = useState<UserProfileDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

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
    } catch (err) {
      console.error('プロフィール取得エラー:', err);
      setError('プロフィールの読み込み中にエラーが発生しました。');
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString?: string) => {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR');
  };

  if (loading) {
    return <div className="profile-page"><div className="loading">読み込み中...</div></div>;
  }

  if (error || !profile) {
    return (
      <div className="profile-page">
        <div className="error">{error || 'プロフィールが見つかりません。'}</div>
      </div>
    );
  }

  // 本人のプロフィールか確認
  const currentUserId = localStorage.getItem('userId');
  const isOwnProfile = currentUserId === userId;

  return (
    <div className="profile-page">
      <div className="profile-header">
        <div className="profile-avatar">
          {profile.avatar ? (
            <img src={profile.avatar} alt={profile.nickname} />
          ) : (
            <div className="avatar-placeholder">{profile.nickname?.charAt(0) || 'U'}</div>
          )}
        </div>
        <div className="profile-info">
          <h1 className="profile-nickname">{profile.nickname}</h1>
          <p className="profile-username">@{profile.username}</p>
          <p className="profile-email">{profile.email}</p>
          <p className="profile-join-date">登録日: {formatDate(profile.joinDate)}</p>
          {isOwnProfile && (
            <button className="btn-edit-profile" onClick={() => navigate(`/profile/${userId}/edit`)}>
              プロフィール編集
            </button>
          )}
        </div>
      </div>

      {profile.bio && (
        <div className="profile-bio">
          <h2>紹介</h2>
          <p>{profile.bio}</p>
        </div>
      )}

      <div className="profile-stats">
        <div className="stat-item">
          <div className="stat-value">{profile.postCount || 0}</div>
          <div className="stat-label">投稿</div>
        </div>
        <div className="stat-item">
          <div className="stat-value">{profile.commentCount || 0}</div>
          <div className="stat-label">コメント</div>
        </div>
      </div>

      <div className="profile-content">
        <section className="recent-posts">
          <h2>最近の投稿</h2>
          {profile.recentPosts && profile.recentPosts.length > 0 ? (
            <ul className="post-list">
              {profile.recentPosts.map((post) => (
                <li key={post.id} className="post-item" onClick={() => navigate(`/post/${post.id}`)}>
                  <h3 className="post-title">{post.title}</h3>
                  <div className="post-meta">
                    <span>{post.boardName}</span>
                    <span>閲覧 {post.viewCount}</span>
                    <span>コメント {post.commentCount}</span>
                    <span className="post-date">{formatDate(post.createdAt)}</span>
                  </div>
                </li>
              ))}
            </ul>
          ) : (
            <p className="empty-message">投稿がありません。</p>
          )}
        </section>

        <section className="recent-comments">
          <h2>最近のコメント</h2>
          {profile.recentComments && profile.recentComments.length > 0 ? (
            <ul className="comment-list">
              {profile.recentComments.map((comment) => (
                <li key={comment.id} className="comment-item">
                  <p className="comment-content">{comment.content}</p>
                  <div className="comment-meta">
                    <span className="comment-date">{formatDate(comment.createdAt)}</span>
                  </div>
                </li>
              ))}
            </ul>
          ) : (
            <p className="empty-message">コメントがありません。</p>
          )}
        </section>
      </div>
    </div>
  );
};

export default ProfilePage;
