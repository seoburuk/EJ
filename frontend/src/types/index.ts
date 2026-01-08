// Type definitions for the application

export interface UserProfile {
  id: number;
  username: string;
  email: string;
  nickname?: string;
  avatar?: string;
  bio?: string;
  postCount?: number;
  commentCount?: number;
  joinDate?: string;
}

export interface Post {
  id: number;
  title: string;
  content: string;
  author: string;
  authorId: number;
  createdAt: string;
  viewCount: number;
  commentCount: number;
  likeCount: number;
  boardId: number;
  boardName?: string;
}

export interface HotPost {
  id: number;
  title: string;
  author: string;
  viewCount: number;
  commentCount: number;
  likeCount: number;
  createdAt: string;
  isHot: boolean;
}

export interface Board {
  id: number;
  name: string;
  description?: string;
  postCount: number;
  category?: string;
  icon?: string;
}

export interface ChatMessage {
  id?: number;
  username: string;
  message: string;
  createdAt?: string;
}

export interface Comment {
  id: number;
  postId: number;
  authorId: number;
  author: string;
  parentId?: number;
  content: string;
  createdAt: string;
  updatedAt?: string;
  status: string;
  isAnonymous: boolean;
}

export interface UserProfileDetail extends UserProfile {
  lastLoginDate?: string;
  status?: string;
  recentPosts?: Post[];
  recentComments?: Comment[];
}

export interface UpdateProfileRequest {
  nickname?: string;
  email?: string;
  avatar?: string;
  bio?: string;
}
