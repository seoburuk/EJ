const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export interface Notification {
  id: number;
  userId: number;
  type: string;  // COMMENT, LIKE, REPLY
  relatedId: number;
  actorId: number;
  actorName: string;
  message: string;
  isRead: boolean;
  createdAt: string;
}

export const notificationApi = {
  // 通知一覧取得
  getNotifications: async (page: number = 1, pageSize: number = 20): Promise<Notification[]> => {
    const response = await fetch(
      `${API_URL}/api/notifications?page=${page}&pageSize=${pageSize}`,
      {
        credentials: 'include',
      }
    );
    return await response.json();
  },

  // 未読通知数取得
  getUnreadCount: async (): Promise<number> => {
    const response = await fetch(`${API_URL}/api/notifications/unread-count`, {
      credentials: 'include',
    });
    const data = await response.json();
    return data.count;
  },

  // 通知を既読にする
  markAsRead: async (id: number): Promise<void> => {
    await fetch(`${API_URL}/api/notifications/${id}/read`, {
      method: 'PUT',
      credentials: 'include',
    });
  },

  // すべての通知を既読にする
  markAllAsRead: async (): Promise<void> => {
    await fetch(`${API_URL}/api/notifications/read-all`, {
      method: 'PUT',
      credentials: 'include',
    });
  },

  // 通知削除
  deleteNotification: async (id: number): Promise<void> => {
    await fetch(`${API_URL}/api/notifications/${id}`, {
      method: 'DELETE',
      credentials: 'include',
    });
  },
};
