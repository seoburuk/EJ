const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export interface AdminStats {
  totalUsers: number;
  totalPosts: number;
  totalComments: number;
}

export const adminApi = {
  // 統計取得
  getStats: async (): Promise<AdminStats> => {
    const response = await fetch(`${API_URL}/api/admin/stats`, {
      credentials: 'include',
    });

    const data = await response.json();
    if (!data.success) {
      throw new Error(data.message);
    }

    return data;
  },

  // すべてのユーザーを取得
  getAllUsers: async (): Promise<any[]> => {
    const response = await fetch(`${API_URL}/api/admin/users`, {
      credentials: 'include',
    });

    const data = await response.json();
    if (!data.success) {
      throw new Error(data.message);
    }

    return data.users;
  },

  // ユーザーステータス変更
  updateUserStatus: async (userId: number, status: string): Promise<any> => {
    const response = await fetch(`${API_URL}/api/admin/users/${userId}/status`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({ status }),
    });

    return await response.json();
  },

  // すべての投稿を取得
  getAllPosts: async (): Promise<any[]> => {
    const response = await fetch(`${API_URL}/api/admin/posts`, {
      credentials: 'include',
    });

    const data = await response.json();
    if (!data.success) {
      throw new Error(data.message);
    }

    return data.posts;
  },

  // 投稿削除
  deletePost: async (postId: number): Promise<any> => {
    const response = await fetch(`${API_URL}/api/admin/posts/${postId}`, {
      method: 'DELETE',
      credentials: 'include',
    });

    return await response.json();
  },

  // コメント削除
  deleteComment: async (commentId: number): Promise<any> => {
    const response = await fetch(`${API_URL}/api/admin/comments/${commentId}`, {
      method: 'DELETE',
      credentials: 'include',
    });

    return await response.json();
  },
};
