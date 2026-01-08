import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ChatWindow from './ChatWindow';
import NotificationDropdown from './NotificationDropdown';
import { notificationApi } from '../../api/notification';
import { messageApi } from '../../api/message';
import './Header.scss';

const Header: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [searchQuery, setSearchQuery] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [nickname, setNickname] = useState('');
  const [isChatOpen, setIsChatOpen] = useState(false);
  const [isNotificationOpen, setIsNotificationOpen] = useState(false);
  const [unreadCount, setUnreadCount] = useState(0);
  const [unreadMessageCount, setUnreadMessageCount] = useState(0);

  useEffect(() => {
    // ログイン状態確認（パス変更時に毎回チェック）
    const userId = localStorage.getItem('userId');
    const userNickname = localStorage.getItem('nickname');

    console.log('Header - userId:', userId);
    console.log('Header - nickname:', userNickname);

    if (userId) {
      setIsLoggedIn(true);
      setNickname(userNickname || '');
      console.log('Header - ログイン状態に設定');
    } else {
      setIsLoggedIn(false);
      setNickname('');
      console.log('Header - ログアウト状態に設定');
    }
  }, [location]);

  // 未読通知数を取得
  useEffect(() => {
    const fetchUnreadCount = async () => {
      if (isLoggedIn) {
        try {
          const count = await notificationApi.getUnreadCount();
          setUnreadCount(count);
        } catch (err) {
          console.error('未読通知数取得失敗:', err);
        }
      }
    };

    fetchUnreadCount();

    // 30秒ごとに更新
    const interval = setInterval(fetchUnreadCount, 30000);

    return () => clearInterval(interval);
  }, [isLoggedIn]);

  // 未読メッセージ数を取得
  useEffect(() => {
    const fetchUnreadMessageCount = async () => {
      if (isLoggedIn) {
        try {
          const count = await messageApi.getUnreadCount();
          setUnreadMessageCount(count);
        } catch (err) {
          console.error('未読メッセージ数取得失敗:', err);
        }
      }
    };

    fetchUnreadMessageCount();

    // 30秒ごとに更新
    const interval = setInterval(fetchUnreadMessageCount, 30000);

    return () => clearInterval(interval);
  }, [isLoggedIn]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/search?q=${encodeURIComponent(searchQuery.trim())}`);
      setSearchQuery('');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    localStorage.removeItem('nickname');
    setIsLoggedIn(false);
    setNickname('');
    navigate('/');
  };

  const boards = [
    { id: 1, name: '中古マーケット' },
    { id: 2, name: '自由掲示板' },
    { id: 3, name: 'イベント掲示板' },
    { id: 4, name: '質問掲示板' },
    { id: 5, name: '人気投稿' },
  ];

  const handleTimetableClick = () => {
    navigate('/timetable');
  };

  const handleLectureReviewClick = () => {
    navigate('/lecture-reviews');
  };

  return (
    <header className="main-header">
      <div className="header-container">
        <div className="logo-section">
          <div className="logo-image" onClick={() => navigate('/')} style={{ cursor: 'pointer' }}>
            {/* TODO: ロゴ画像追加 */}
            <span className="logo-text">ポリ</span>
          </div>
        </div>

        <div className="title-section">
          <h1 className="site-title" onClick={() => navigate('/')} style={{ cursor: 'pointer' }}>
            GLOBALIN
          </h1>
          <p className="site-subtitle">일본 유학생 커뮤니티</p>
        </div>

        <div className="search-section">
          <form onSubmit={handleSearch} className="search-form">
            <input
              type="text"
              placeholder="投稿を検索..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="search-input"
            />
            <button type="submit" className="search-button">
              🔍
            </button>
          </form>
        </div>

        <div className="chat-section">
          <button
            className="chat-button"
            onClick={() => setIsChatOpen(!isChatOpen)}
            title="チャットルーム"
          >
            💬
          </button>
        </div>

        <div className="lecture-review-section">
          <button
            className="lecture-review-button"
            onClick={handleLectureReviewClick}
            title="講義レビュー"
          >
            📝
          </button>
        </div>

        {isLoggedIn && (
          <>
            <div className="timetable-section">
              <button
                className="timetable-button"
                onClick={handleTimetableClick}
                title="時間割"
              >
                📅
              </button>
            </div>

            <div className="message-section">
              <button
                className="message-button"
                onClick={() => navigate('/messages')}
                title="メッセージ"
              >
                ✉️
                {unreadMessageCount > 0 && (
                  <span className="message-badge">{unreadMessageCount > 99 ? '99+' : unreadMessageCount}</span>
                )}
              </button>
            </div>

            <div className="notification-section">
              <button
                className="notification-button"
                onClick={() => setIsNotificationOpen(!isNotificationOpen)}
                title="通知"
              >
                🔔
                {unreadCount > 0 && (
                  <span className="notification-badge">{unreadCount > 99 ? '99+' : unreadCount}</span>
                )}
              </button>
              <NotificationDropdown
                isOpen={isNotificationOpen}
                onClose={() => setIsNotificationOpen(false)}
              />
            </div>
          </>
        )}

        <div className="auth-section">
          {isLoggedIn ? (
            <div className="user-info">
              <span className="user-nickname">{nickname}様</span>
              <button className="logout-button" onClick={handleLogout}>
                ログアウト
              </button>
            </div>
          ) : (
            <div className="auth-buttons">
              <button className="login-button" onClick={() => navigate('/login')}>
                ログイン
              </button>
              <button className="register-button" onClick={() => navigate('/register')}>
                会員登録
              </button>
            </div>
          )}
        </div>
      </div>

      {/* 掲示板ナビゲーション */}
      <nav className="board-nav">
        <div className="board-nav-container">
          {boards.map((board) => (
            <button
              key={board.id}
              className="board-nav-item"
              onClick={() => navigate(`/board/${board.id}`)}
            >
              {board.name}
            </button>
          ))}
        </div>
      </nav>

      {/* チャットウィンドウ */}
      <ChatWindow isOpen={isChatOpen} onClose={() => setIsChatOpen(false)} />
    </header>
  );
};

export default Header;
