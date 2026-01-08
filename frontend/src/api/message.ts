const API_URL = 'http://localhost:8080';

export interface Message {
  id: number;
  senderId: number;
  receiverId: number;
  title: string;
  content: string;
  isRead: boolean;
  createdAt: string;
  deletedBySender: boolean;
  deletedByReceiver: boolean;
  senderNickname: string;
  receiverNickname: string;
}

export const messageApi = {
  // メッセージ送信
  sendMessage: async (receiverId: number, title: string, content: string): Promise<any> => {
    const response = await fetch(`${API_URL}/api/messages`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({ receiverId, title, content }),
    });

    const data = await response.json();
    if (!data.success) {
      throw new Error(data.message);
    }
    return data;
  },

  // 受け取ったメッセージリスト
  getReceivedMessages: async (offset: number = 0, limit: number = 20): Promise<{ messages: Message[]; total: number }> => {
    const response = await fetch(`${API_URL}/api/messages/received?offset=${offset}&limit=${limit}`, {
      credentials: 'include',
    });

    const data = await response.json();
    if (!data.success) {
      throw new Error(data.message);
    }
    return { messages: data.messages, total: data.total };
  },

  // 送信されたメッセージリスト
  getSentMessages: async (offset: number = 0, limit: number = 20): Promise<{ messages: Message[]; total: number }> => {
    const response = await fetch(`${API_URL}/api/messages/sent?offset=${offset}&limit=${limit}`, {
      credentials: 'include',
    });

    const data = await response.json();
    if (!data.success) {
      throw new Error(data.message);
    }
    return { messages: data.messages, total: data.total };
  },

  // メッセージ取得
  getMessage: async (id: number): Promise<Message> => {
    const response = await fetch(`${API_URL}/api/messages/${id}`, {
      credentials: 'include',
    });

    const data = await response.json();
    if (!data.success) {
      throw new Error(data.message);
    }
    return data.message;
  },

  // 未読メッセージ数
  getUnreadCount: async (): Promise<number> => {
    const response = await fetch(`${API_URL}/api/messages/unread/count`, {
      credentials: 'include',
    });

    const data = await response.json();
    if (!data.success) {
      throw new Error(data.message);
    }
    return data.count;
  },

  // メッセージ削除
  deleteMessage: async (id: number): Promise<any> => {
    const response = await fetch(`${API_URL}/api/messages/${id}`, {
      method: 'DELETE',
      credentials: 'include',
    });

    const data = await response.json();
    if (!data.success) {
      throw new Error(data.message);
    }
    return data;
  },
};
