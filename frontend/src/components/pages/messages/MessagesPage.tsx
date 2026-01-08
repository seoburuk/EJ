import React, { useState, useEffect } from 'react';
import { messageApi, Message } from '../../../api/message';
import './MessagesPage.scss';

const MessagesPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'received' | 'sent' | 'compose'>('received');
  const [receivedMessages, setReceivedMessages] = useState<Message[]>([]);
  const [sentMessages, setSentMessages] = useState<Message[]>([]);
  const [selectedMessage, setSelectedMessage] = useState<Message | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [unreadCount, setUnreadCount] = useState(0);

  // メッセージ作成フォーム
  const [receiverId, setReceiverId] = useState('');
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  useEffect(() => {
    loadMessages();
    loadUnreadCount();
  }, [activeTab]);

  const loadMessages = async () => {
    try {
      setLoading(true);
      if (activeTab === 'received') {
        const { messages } = await messageApi.getReceivedMessages();
        setReceivedMessages(messages);
      } else if (activeTab === 'sent') {
        const { messages } = await messageApi.getSentMessages();
        setSentMessages(messages);
      }
      setLoading(false);
    } catch (err: any) {
      setError(err.message || 'メッセージの読み込みに失敗しました。');
      setLoading(false);
    }
  };

  const loadUnreadCount = async () => {
    try {
      const count = await messageApi.getUnreadCount();
      setUnreadCount(count);
    } catch (err) {
      console.error('Failed to load unread count', err);
    }
  };

  const handleMessageClick = async (message: Message) => {
    try {
      const fullMessage = await messageApi.getMessage(message.id);
      setSelectedMessage(fullMessage);
      // 既読処理後、未読メッセージ数を更新
      if (activeTab === 'received' && !message.isRead) {
        loadUnreadCount();
        loadMessages();
      }
    } catch (err: any) {
      alert(err.message || 'メッセージの読み込みに失敗しました。');
    }
  };

  const handleDeleteMessage = async (id: number) => {
    if (!window.confirm('メッセージを削除しますか？')) return;

    try {
      await messageApi.deleteMessage(id);
      alert('メッセージが削除されました。');
      setSelectedMessage(null);
      loadMessages();
      loadUnreadCount();
    } catch (err: any) {
      alert(err.message || 'メッセージの削除に失敗しました。');
    }
  };

  const handleSendMessage = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!receiverId || !title || !content) {
      alert('すべての項目を入力してください。');
      return;
    }

    try {
      await messageApi.sendMessage(Number(receiverId), title, content);
      alert('メッセージが送信されました。');
      setReceiverId('');
      setTitle('');
      setContent('');
      setActiveTab('sent');
    } catch (err: any) {
      alert(err.message || 'メッセージの送信に失敗しました。');
    }
  };

  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    return date.toLocaleString('ko-KR');
  };

  if (loading && activeTab !== 'compose') {
    return (
      <div className="messages-page">
        <div className="loading">読み込み中...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="messages-page">
        <div className="error-message">{error}</div>
      </div>
    );
  }

  const currentMessages = activeTab === 'received' ? receivedMessages : sentMessages;

  return (
    <div className="messages-page">
      <div className="messages-header">
        <h1>メッセージ</h1>
        {unreadCount > 0 && (
          <span className="unread-badge">{unreadCount}件の新しいメッセージ</span>
        )}
      </div>

      <div className="messages-tabs">
        <button
          className={`tab-button ${activeTab === 'received' ? 'active' : ''}`}
          onClick={() => {
            setActiveTab('received');
            setSelectedMessage(null);
          }}
        >
          受信メッセージ
        </button>
        <button
          className={`tab-button ${activeTab === 'sent' ? 'active' : ''}`}
          onClick={() => {
            setActiveTab('sent');
            setSelectedMessage(null);
          }}
        >
          送信メッセージ
        </button>
        <button
          className={`tab-button ${activeTab === 'compose' ? 'active' : ''}`}
          onClick={() => {
            setActiveTab('compose');
            setSelectedMessage(null);
          }}
        >
          メッセージ作成
        </button>
      </div>

      <div className="messages-content">
        {activeTab === 'compose' ? (
          <div className="compose-form">
            <h2>新しいメッセージ作成</h2>
            <form onSubmit={handleSendMessage}>
              <div className="form-group">
                <label>受信者ID</label>
                <input
                  type="number"
                  value={receiverId}
                  onChange={(e) => setReceiverId(e.target.value)}
                  placeholder="受信者のユーザーIDを入力してください"
                  required
                />
              </div>
              <div className="form-group">
                <label>タイトル</label>
                <input
                  type="text"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  placeholder="タイトルを入力してください"
                  required
                />
              </div>
              <div className="form-group">
                <label>内容</label>
                <textarea
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                  placeholder="内容を入力してください"
                  rows={10}
                  required
                />
              </div>
              <div className="form-actions">
                <button type="submit" className="send-btn">送信</button>
                <button
                  type="button"
                  className="cancel-btn"
                  onClick={() => {
                    setReceiverId('');
                    setTitle('');
                    setContent('');
                    setActiveTab('received');
                  }}
                >
                  キャンセル
                </button>
              </div>
            </form>
          </div>
        ) : selectedMessage ? (
          <div className="message-detail">
            <button className="back-btn" onClick={() => setSelectedMessage(null)}>
              ← リストに戻る
            </button>
            <div className="message-header">
              <h2>{selectedMessage.title}</h2>
              <div className="message-meta">
                <span className="sender">
                  {activeTab === 'received' ? '送信者' : '受信者'}:{' '}
                  {activeTab === 'received'
                    ? selectedMessage.senderNickname
                    : selectedMessage.receiverNickname}
                </span>
                <span className="date">{formatDate(selectedMessage.createdAt)}</span>
              </div>
            </div>
            <div className="message-body">
              <p>{selectedMessage.content}</p>
            </div>
            <div className="message-actions">
              <button
                className="delete-btn"
                onClick={() => handleDeleteMessage(selectedMessage.id)}
              >
                削除
              </button>
            </div>
          </div>
        ) : (
          <div className="messages-list">
            <div className="list-header">
              <h2>{activeTab === 'received' ? '受信メッセージ' : '送信メッセージ'}</h2>
            </div>
            {currentMessages.length === 0 ? (
              <p className="no-messages">メッセージがありません。</p>
            ) : (
              <table className="messages-table">
                <thead>
                  <tr>
                    <th className="status-col"></th>
                    <th className="from-col">
                      {activeTab === 'received' ? '送信者' : '受信者'}
                    </th>
                    <th className="title-col">タイトル</th>
                    <th className="date-col">日付</th>
                  </tr>
                </thead>
                <tbody>
                  {currentMessages.map((message) => (
                    <tr
                      key={message.id}
                      onClick={() => handleMessageClick(message)}
                      className={message.isRead ? 'read' : 'unread'}
                    >
                      <td className="status-col">
                        {activeTab === 'received' && !message.isRead && (
                          <span className="new-badge">N</span>
                        )}
                      </td>
                      <td className="from-col">
                        {activeTab === 'received'
                          ? message.senderNickname
                          : message.receiverNickname}
                      </td>
                      <td className="title-col">{message.title}</td>
                      <td className="date-col">{formatDate(message.createdAt)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default MessagesPage;
