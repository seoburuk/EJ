const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  nickname: string;
}

export interface RegisterResponse {
  success: boolean;
  message: string;
  userId?: number;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  userId?: number;
  username?: string;
  nickname?: string;
}

export const authApi = {
  // 登録
  register: async (data: RegisterRequest): Promise<RegisterResponse> => {
    const response = await fetch(`${API_URL}/api/auth/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include', // セッションクッキーを保存して送信するために必要
      body: JSON.stringify(data),
    });

    return await response.json();
  },

  // ログイン
  login: async (data: LoginRequest): Promise<LoginResponse> => {
    const response = await fetch(`${API_URL}/api/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include', // セッションクッキーを保存して送信するために必要
      body: JSON.stringify(data),
    });

    return await response.json();
  },
};
