const API_BASE_URL = 'http://localhost:8080/api';

export interface LectureReview {
  id?: number;
  userId?: number;
  courseName: string;
  professor: string;
  difficulty: number;     // 1-5
  workload: number;       // 1-5
  satisfaction: number;   // 1-5
  reviewText?: string;
  semester?: string;
  year?: number;
  isAnonymous?: boolean;
  createdAt?: string;
  updatedAt?: string;
  authorNickname?: string;
}

export interface ReviewSearchResult {
  reviews: LectureReview[];
  averageRatings: {
    avgDifficulty: number;
    avgWorkload: number;
    avgSatisfaction: number;
  };
  count: number;
}

export interface ApiResponse {
  success: boolean;
  message: string;
  review?: LectureReview;
}

export const lectureReviewApi = {
  // レビュー作成
  createReview: async (review: LectureReview): Promise<ApiResponse> => {
    const response = await fetch(`${API_BASE_URL}/lecture-reviews`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(review),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'レビュー作成に失敗しました。');
    }

    return response.json();
  },

  // レビュー取得
  getReview: async (id: number): Promise<LectureReview> => {
    const response = await fetch(`${API_BASE_URL}/lecture-reviews/${id}`, {
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('レビュー取得に失敗しました。');
    }

    return response.json();
  },

  // レビュー更新
  updateReview: async (id: number, review: LectureReview): Promise<ApiResponse> => {
    const response = await fetch(`${API_BASE_URL}/lecture-reviews/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(review),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'レビュー更新に失敗しました。');
    }

    return response.json();
  },

  // レビュー削除
  deleteReview: async (id: number): Promise<ApiResponse> => {
    const response = await fetch(`${API_BASE_URL}/lecture-reviews/${id}`, {
      method: 'DELETE',
      credentials: 'include',
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'レビュー削除に失敗しました。');
    }

    return response.json();
  },

  // コース名/教授名で検索
  searchReviews: async (keyword?: string): Promise<LectureReview[]> => {
    const url = keyword
      ? `${API_BASE_URL}/lecture-reviews/search?keyword=${encodeURIComponent(keyword)}`
      : `${API_BASE_URL}/lecture-reviews/search`;

    const response = await fetch(url, {
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('検索に失敗しました。');
    }

    return response.json();
  },

  // コース名+教授名で詳細検索
  getReviewsByCourse: async (courseName: string, professor: string): Promise<ReviewSearchResult> => {
    const response = await fetch(
      `${API_BASE_URL}/lecture-reviews/course?courseName=${encodeURIComponent(courseName)}&professor=${encodeURIComponent(professor)}`,
      {
        credentials: 'include',
      }
    );

    if (!response.ok) {
      throw new Error('コース情報取得に失敗しました。');
    }

    return response.json();
  },

  // 自分が作成したレビューリスト
  getMyReviews: async (): Promise<LectureReview[]> => {
    const response = await fetch(`${API_BASE_URL}/lecture-reviews/my-reviews`, {
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('自分のレビュー取得に失敗しました。');
    }

    return response.json();
  },

  // すべてのレビュー取得 (ページング)
  getAllReviews: async (limit: number = 20, offset: number = 0): Promise<{ reviews: LectureReview[]; totalCount: number }> => {
    const response = await fetch(
      `${API_BASE_URL}/lecture-reviews?limit=${limit}&offset=${offset}`,
      {
        credentials: 'include',
      }
    );

    if (!response.ok) {
      throw new Error('レビューリスト取得に失敗しました。');
    }

    return response.json();
  },
};
