const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export interface PostImage {
  id: number;
  postId: number;
  imageUrl: string;
  originalFilename: string;
  fileSize?: number;
  displayOrder: number;
  createdAt: string;
}

export interface Post {
  id: number;
  boardId: number;
  boardName: string;
  authorId: number;
  author: string;
  title: string;
  content: string;
  viewCount: number;
  likeCount: number;
  commentCount: number;
  imageCount?: number;
  images?: PostImage[];
  createdAt: number;
  updatedAt?: number;
  isAnonymous: boolean;
}

export interface CreatePostRequest {
  boardId: number;
  title: string;
  content: string;
  isAnonymous?: boolean;
  imageUrls?: string[];
}

export interface UpdatePostRequest {
  title: string;
  content: string;
}

export interface PostResponse {
  success: boolean;
  message: string;
  postId?: number;
}

export const postApi = {
  // 投稿作成
  createPost: async (data: CreatePostRequest): Promise<PostResponse> => {
    const response = await fetch(`${API_URL}/api/posts`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include', // セッションクッキー込み
      body: JSON.stringify(data),
    });

    return await response.json();
  },

  // 投稿取得
  getPost: async (id: number): Promise<Post> => {
    const response = await fetch(`${API_URL}/api/posts/${id}`, {
      credentials: 'include',
    });

    return await response.json();
  },

  // 掲示板別投稿リスト
  getPostsByBoard: async (boardId: number, page: number = 1, pageSize: number = 20): Promise<Post[]> => {
    const response = await fetch(
      `${API_URL}/api/posts/board/${boardId}?page=${page}&pageSize=${pageSize}`,
      {
        credentials: 'include',
      }
    );

    return await response.json();
  },

  // 全投稿リスト
  getAllPosts: async (page: number = 1, pageSize: number = 20): Promise<Post[]> => {
    const response = await fetch(
      `${API_URL}/api/posts?page=${page}&pageSize=${pageSize}`,
      {
        credentials: 'include',
      }
    );

    return await response.json();
  },

  // 自分の投稿リスト
  getMyPosts: async (page: number = 1, pageSize: number = 20): Promise<Post[]> => {
    const response = await fetch(
      `${API_URL}/api/posts/my?page=${page}&pageSize=${pageSize}`,
      {
        credentials: 'include',
      }
    );

    return await response.json();
  },

  // 投稿更新
  updatePost: async (id: number, data: UpdatePostRequest): Promise<PostResponse> => {
    const response = await fetch(`${API_URL}/api/posts/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(data),
    });

    return await response.json();
  },

  // 投稿削除
  deletePost: async (id: number): Promise<PostResponse> => {
    const response = await fetch(`${API_URL}/api/posts/${id}`, {
      method: 'DELETE',
      credentials: 'include',
    });

    return await response.json();
  },

  // 投稿検索
  searchPosts: async (keyword: string, page: number = 1, pageSize: number = 20): Promise<Post[]> => {
    const response = await fetch(
      `${API_URL}/api/posts/search?keyword=${encodeURIComponent(keyword)}&page=${page}&pageSize=${pageSize}`,
      {
        credentials: 'include',
      }
    );

    return await response.json();
  },

  // いいねトグル
  toggleLike: async (id: number): Promise<PostResponse> => {
    const response = await fetch(`${API_URL}/api/posts/${id}/like`, {
      method: 'POST',
      credentials: 'include',
    });

    return await response.json();
  },

  // いいね数取得
  getLikeCount: async (id: number): Promise<number> => {
    const response = await fetch(`${API_URL}/api/posts/${id}/like/count`, {
      credentials: 'include',
    });

    return await response.json();
  },

  // いいね状態取得
  getLikeStatus: async (id: number): Promise<boolean> => {
    const response = await fetch(`${API_URL}/api/posts/${id}/like/status`, {
      credentials: 'include',
    });

    return await response.json();
  },
};

// 掲示板リスト API (boards)
export interface Board {
  id: number;
  name: string;
  description: string;
  category: string;
  icon: string;
  postCount: number;
}

export const boardApi = {
  // 掲示板リスト取得 (将来実装予定)
  getAllBoards: async (): Promise<Board[]> => {
    const response = await fetch(`${API_URL}/api/boards`, {
      credentials: 'include',
    });

    return await response.json();
  },
};
