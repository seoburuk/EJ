import React from 'react';
import { UserProfile } from '../../../types';
import { useNavigate } from 'react-router-dom';
import './LeftSidebar.scss';

interface LeftSidebarProps {
  userProfile?: UserProfile | null;
}

const LeftSidebar: React.FC<LeftSidebarProps> = ({ userProfile }) => {
  const navigate = useNavigate();

  if (!userProfile) {
    return (
      <div className="left-sidebar">
        <div className="user-profile-card">
          <div className="login-prompt">
            <h3>ログインが必要です</h3>
            <p>コミュニティの様々な機能を利用するにはログインしてください。</p>
            <button className="login-btn" onClick={() => navigate('/login')}>
              ログイン
            </button>
            <button className="register-btn" onClick={() => navigate('/register')}>
              会員登録
            </button>
          </div>
        </div>
      </div>
    );
  }

  const handleProfileClick = () => {
    if (userProfile?.id) {
      navigate(`/profile/${userProfile.id}`);
    }
  };

  return (
    <div className="left-sidebar">
      <div className="user-profile-card" onClick={handleProfileClick}>
        <div className="user-avatar">
          {userProfile.avatar ? (
            <img src={userProfile.avatar} alt={userProfile.username || userProfile.nickname} />
          ) : (
            <div className="avatar-placeholder">
              {(userProfile.nickname || userProfile.username || 'U').charAt(0).toUpperCase()}
            </div>
          )}
        </div>
        <div className="user-info">
          <h3 className="username">{userProfile.nickname || userProfile.username || 'ユーザー'}</h3>
          {userProfile.email && <p className="email">{userProfile.email}</p>}
        </div>
        <div className="user-stats">
          <div className="stat-item">
            <span className="stat-label">投稿</span>
            <span className="stat-value">{userProfile.postCount || 0}</span>
          </div>
          <div className="stat-item">
            <span className="stat-label">コメント</span>
            <span className="stat-value">{userProfile.commentCount || 0}</span>
          </div>
        </div>
        {userProfile.joinDate && (
          <div className="join-date">
            登録日: {new Date(userProfile.joinDate).toLocaleDateString('ja-JP')}
          </div>
        )}
        <div className="profile-link">
          プロフィール詳細 →
        </div>
      </div>
    </div>
  );
};

export default LeftSidebar;
