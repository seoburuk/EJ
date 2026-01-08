const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export interface Comment {
  id: number;
  postId: number;
  authorId: number;
  parentId: number | null;
  content: string;
  createdAt: number;
  updatedAt?: number;
  status: string;
  isAnonymous: boolean;
  author: string;
  likeCount: number;
}

export interface CreateCommentRequest {
  postId: number;
  parentId?: number | null;
  content: string;
  isAnonymous?: boolean;
}

export interface UpdateCommentRequest {
  content: string;
}

export interface CommentResponse {
  success: boolean;
  message: string;
  commentId?: number;
  isLiked?: boolean;
}

export const commentApi = {
  // コメント作成
  createComment: async (data: CreateCommentRequest): Promise<CommentResponse> => {
    const response = await fetch(`${API_URL}/api/comments`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(data),
    });

    return await response.json();
  },

  // 投稿のすべてのコメントを取得
  getCommentsByPost: async (postId: number): Promise<Comment[]> => {
    const response = await fetch(`${API_URL}/api/comments/post/${postId}`, {
      credentials: 'include',
    });

    return await response.json();
  },

  // コメント更新
  updateComment: async (commentId: number, data: UpdateCommentRequest): Promise<CommentResponse> => {
    const response = await fetch(`${API_URL}/api/comments/${commentId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(data),
    });

    return await response.json();
  },

  // コメント削除
  deleteComment: async (commentId: number): Promise<CommentResponse> => {
    const response = await fetch(`${API_URL}/api/comments/${commentId}`, {
      method: 'DELETE',
      credentials: 'include',
    });

    return await response.json();
  },

  // いいねトグル
  toggleCommentLike: async (commentId: number): Promise<CommentResponse> => {
    const response = await fetch(`${API_URL}/api/comments/${commentId}/like`, {
      method: 'POST',
      credentials: 'include',
    });

    return await response.json();
  },

  // いいね状態確認
  isCommentLiked: async (commentId: number): Promise<boolean> => {
    const response = await fetch(`${API_URL}/api/comments/${commentId}/like/status`, {
      credentials: 'include',
    });

    return await response.json();
  },
};
