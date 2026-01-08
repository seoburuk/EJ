const API_BASE_URL = 'http://localhost:8080/api';

export interface Report {
  id: number;
  reporterId: number;
  postId?: number;
  userId?: number;
  reason: string;
  status: string;
  createdAt: string;
  resolvedAt?: string;
  adminNote?: string;
  reporterNickname?: string;
  postTitle?: string;
  reportedUserNickname?: string;
}

export interface ReportResponse {
  success: boolean;
  message: string;
}

export const reportApi = {
  // 投稿削除
  reportPost: async (postId: number, reason: string): Promise<ReportResponse> => {
    const response = await fetch(`${API_BASE_URL}/reports/post/${postId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({ reason }),
    });

    if (!response.ok) {
      throw new Error('投稿削除に失敗しました。');
    }

    return response.json();
  },

  // ユーザー報告
  reportUser: async (userId: number, reason: string): Promise<ReportResponse> => {
    const response = await fetch(`${API_BASE_URL}/reports/user/${userId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({ reason }),
    });

    if (!response.ok) {
      throw new Error('ユーザー報告に失敗しました。');
    }

    return response.json();
  },

  // すべての報告を取得 (管理者用)
  getAllReports: async (): Promise<Report[]> => {
    const response = await fetch(`${API_BASE_URL}/reports`, {
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('報告リスト取得に失敗しました。');
    }

    return response.json();
  },

  // 保留中の報告を取得 (管理者用)
  getPendingReports: async (): Promise<Report[]> => {
    const response = await fetch(`${API_BASE_URL}/reports/pending`, {
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('保留中の報告取得に失敗しました。');
    }

    return response.json();
  },

  // 保留中の報告数 (管理者用)
  getPendingReportCount: async (): Promise<number> => {
    const response = await fetch(`${API_BASE_URL}/reports/pending/count`, {
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('報告数取得に失敗しました。');
    }

    return response.json();
  },

  // 報告処理 (承認)
  resolveReport: async (id: number, adminNote?: string): Promise<ReportResponse> => {
    const response = await fetch(`${API_BASE_URL}/reports/${id}/resolve`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({ adminNote: adminNote || '' }),
    });

    if (!response.ok) {
      throw new Error('報告処理に失敗しました。');
    }

    return response.json();
  },

  // 報告拒否
  rejectReport: async (id: number, adminNote?: string): Promise<ReportResponse> => {
    const response = await fetch(`${API_BASE_URL}/reports/${id}/reject`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({ adminNote: adminNote || '' }),
    });

    if (!response.ok) {
      throw new Error('報告拒否に失敗しました。');
    }

    return response.json();
  },

  // 報告削除 (管理者用)
  deleteReport: async (id: number): Promise<ReportResponse> => {
    const response = await fetch(`${API_BASE_URL}/reports/${id}`, {
      method: 'DELETE',
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('報告削除に失敗しました。');
    }

    return response.json();
  },
};
