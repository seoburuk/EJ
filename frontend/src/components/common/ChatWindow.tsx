import React, { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { ChatMessage } from '../../types';
import './ChatWindow.scss';

interface ChatWindowProps {
  isOpen: boolean;
  onClose: () => void;
}

const ChatWindow: React.FC<ChatWindowProps> = ({ isOpen, onClose }) => {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [messageInput, setMessageInput] = useState('');
  const [username, setUsername] = useState('');
  const [isConnected, setIsConnected] = useState(false);
  const [isJoined, setIsJoined] = useState(false);
  const stompClientRef = useRef<Client | null>(null);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  // メッセージエリア自動スクロール
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  // ログインユーザー情報を取得
  useEffect(() => {
    const loggedInUsername = localStorage.getItem('username');
    const loggedInNickname = localStorage.getItem('nickname');

    if (loggedInNickname) {
      setUsername(loggedInNickname);
    } else if (loggedInUsername) {
      setUsername(loggedInUsername);
    }
  }, []);

  // WebSocket接続
  const connect = () => {
    const apiUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080';

    const client = new Client({
      webSocketFactory: () => new SockJS(`${apiUrl}/ws-chat`) as any,
      onConnect: () => {
        setIsConnected(true);
        console.log('WebSocket接続成功');

        // メッセージ購読
        client.subscribe('/topic/messages', (message) => {
          const receivedMessage: ChatMessage = JSON.parse(message.body);
          setMessages((prev) => [...prev, receivedMessage]);
        });

        // ログインユーザーなら自動入場
        if (username) {
          autoJoinChat(client);
        }
      },
      onStompError: (error: any) => {
        console.error('WebSocket接続失敗:', error);
        setIsConnected(false);
      },
      debug: (str) => {
        console.log('STOMP:', str);
      }
    });

    client.activate();
    stompClientRef.current = client;
  };

  // WebSocket接続解除
  const disconnect = () => {
    if (stompClientRef.current && isConnected) {
      stompClientRef.current.deactivate();
      console.log('WebSocket接続解除');
      setIsConnected(false);
    }
  };

  // 自動チャット入場（ログインユーザー）
  const autoJoinChat = (client: Client) => {
    if (username) {
      const joinMessage = {
        username: username,
        message: '',
      };

      client.publish({
        destination: '/app/chat.addUser',
        body: JSON.stringify(joinMessage)
      });
      setIsJoined(true);
    }
  };

  // 手動チャット入場（非ログインユーザー）
  const joinChat = () => {
    if (!username.trim()) {
      alert('ニックネームを入力してください！');
      return;
    }

    if (stompClientRef.current && isConnected) {
      const joinMessage = {
        username: username.trim(),
        message: '',
      };

      stompClientRef.current.publish({
        destination: '/app/chat.addUser',
        body: JSON.stringify(joinMessage)
      });
      setIsJoined(true);
    }
  };

  // メッセージ送信
  const sendMessage = () => {
    if (!messageInput.trim()) return;

    if (stompClientRef.current && isConnected && isJoined) {
      const chatMessage = {
        username: username,
        message: messageInput.trim(),
      };

      stompClientRef.current.publish({
        destination: '/app/chat.sendMessage',
        body: JSON.stringify(chatMessage)
      });
      setMessageInput('');
    }
  };

  // Enterキーでメッセージ送信
  const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      if (isJoined) {
        sendMessage();
      } else {
        joinChat();
      }
    }
  };

  // コンポーネントマウント時にWebSocket接続
  useEffect(() => {
    if (isOpen) {
      connect();

      // 既存メッセージ読み込み
      const apiUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080';
      fetch(`${apiUrl}/api/chat/recent?limit=50`)
        .then((res) => res.json())
        .then((data: ChatMessage[]) => {
          setMessages(data.reverse()); // 時間順ソート
        })
        .catch((error) => console.error('メッセージ読み込み失敗:', error));
    }

    return () => {
      disconnect();
    };
  }, [isOpen, username]);

  if (!isOpen) return null;

  return (
    <div className="chat-window">
      <div className="chat-window__header">
        <h3 className="chat-window__title">チャットルーム</h3>
        <button className="chat-window__close" onClick={onClose}>
          ✕
        </button>
      </div>

      {!isJoined ? (
        <div className="chat-window__join">
          {username ? (
            <div className="chat-window__connecting">
              <p>チャットルーム接続中...</p>
              <p className="chat-window__username-display">{username}様として入場します</p>
            </div>
          ) : (
            <>
              <h4>ログインが必要です</h4>
              <div className="chat-window__join-form">
                <input
                  type="text"
                  placeholder="ニックネーム入力（仮）"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  onKeyPress={handleKeyPress}
                  className="chat-window__username-input"
                />
                <button onClick={joinChat} className="chat-window__join-button">
                  入場
                </button>
              </div>
              <p className="chat-window__login-notice">
                <a href="/login">ログイン</a>すると自動的に入場します
              </p>
            </>
          )}
        </div>
      ) : (
        <>
          <div className="chat-window__user-info">
            <span className="chat-window__current-user">{username}様としてチャット中</span>
          </div>

          <div className="chat-window__messages">
            {messages.map((msg, index) => (
              <div
                key={index}
                className={`chat-message ${
                  msg.username === 'System' ? 'chat-message--system' : ''
                } ${msg.username === username ? 'chat-message--own' : ''}`}
              >
                <span className="chat-message__username">{msg.username}</span>
                <span className="chat-message__text">{msg.message}</span>
              </div>
            ))}
            <div ref={messagesEndRef} />
          </div>

          <div className="chat-window__input">
            <input
              type="text"
              placeholder="メッセージを入力してください..."
              value={messageInput}
              onChange={(e) => setMessageInput(e.target.value)}
              onKeyPress={handleKeyPress}
              className="chat-window__message-input"
            />
            <button onClick={sendMessage} className="chat-window__send-button">
              送信
            </button>
          </div>
        </>
      )}

      <div className="chat-window__status">
        {isConnected ? (
          <span className="chat-window__status--connected">● 接続済み</span>
        ) : (
          <span className="chat-window__status--disconnected">● 接続切断</span>
        )}
      </div>
    </div>
  );
};

export default ChatWindow;
