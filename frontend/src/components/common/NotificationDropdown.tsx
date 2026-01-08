import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { notificationApi, Notification } from '../../api/notification';
import './NotificationDropdown.scss';

interface NotificationDropdownProps {
  isOpen: boolean;
  onClose: () => void;
}

const NotificationDropdown: React.FC<NotificationDropdownProps> = ({ isOpen, onClose }) => {
  const navigate = useNavigate();
  const dropdownRef = useRef<HTMLDivElement>(null);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isOpen) {
      loadNotifications();
    }
  }, [isOpen]);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        onClose();
      }
    };

    if (isOpen) {
      document.addEventListener('mousedown', handleClickOutside);
    }

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isOpen, onClose]);

  const loadNotifications = async () => {
    setLoading(true);
    try {
      const data = await notificationApi.getNotifications(1, 10);
      setNotifications(data);
    } catch (err) {
      console.error('通知読み込み失敗:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleNotificationClick = async (notification: Notification) => {
    // 既読処理
    if (!notification.isRead) {
      await notificationApi.markAsRead(notification.id);
    }

    // 関連ページに移動
    if (notification.type === 'LIKE' || notification.type === 'COMMENT') {
      navigate(`/posts/${notification.relatedId}`);
    }

    onClose();
  };

  const handleMarkAllAsRead = async () => {
    try {
      await notificationApi.markAllAsRead();
      setNotifications(notifications.map(n => ({ ...n, isRead: true })));
    } catch (err) {
      console.error('全て既読処理失敗:', err);
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffMins < 1) return 'たった今';
    if (diffMins < 60) return `${diffMins}分前`;
    if (diffHours < 24) return `${diffHours}時間前`;
    if (diffDays < 7) return `${diffDays}日前`;

    return date.toLocaleDateString('ja-JP');
  };

  const getNotificationIcon = (type: string) => {
    switch (type) {
      case 'LIKE':
        return '❤️';
      case 'COMMENT':
        return '💬';
      case 'REPLY':
        return '↩️';
      default:
        return '🔔';
    }
  };

  if (!isOpen) return null;

  return (
    <div className="notification-dropdown" ref={dropdownRef}>
      <div className="dropdown-header">
        <h3>通知</h3>
        {notifications.some(n => !n.isRead) && (
          <button className="mark-all-read" onClick={handleMarkAllAsRead}>
            全て既読
          </button>
        )}
      </div>

      <div className="notifications-list">
        {loading ? (
          <div className="loading">読み込み中...</div>
        ) : notifications.length === 0 ? (
          <div className="empty">新しい通知はありません</div>
        ) : (
          notifications.map((notification) => (
            <div
              key={notification.id}
              className={`notification-item ${!notification.isRead ? 'unread' : ''}`}
              onClick={() => handleNotificationClick(notification)}
            >
              <div className="notification-icon">{getNotificationIcon(notification.type)}</div>
              <div className="notification-content">
                <p className="notification-message">{notification.message}</p>
                <span className="notification-time">{formatDate(notification.createdAt)}</span>
              </div>
              {!notification.isRead && <div className="unread-dot"></div>}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default NotificationDropdown;
