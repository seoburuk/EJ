import React, { useState, useEffect } from 'react';
import { adminApi, AdminStats } from '../../../api/admin';
import './AdminPage.scss';

interface User {
  id: number;
  username: string;
  email: string;
  nickname: string;
  status: string;
  role: string;
  joinDate: string;
}

interface Post {
  id: number;
  title: string;
  author: string;
  boardName: string;
  viewCount: number;
  createdAt: string;
  status: string;
}

const AdminPage: React.FC = () => {
  const [stats, setStats] = useState<AdminStats | null>(null);
  const [users, setUsers] = useState<User[]>([]);
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState<'dashboard' | 'users' | 'posts'>('dashboard');
  const [userSearchTerm, setUserSearchTerm] = useState('');
  const [postSearchTerm, setPostSearchTerm] = useState('');

  useEffect(() => {
    loadStats();
  }, []);

  useEffect(() => {
    if (activeTab === 'users') {
      loadUsers();
    } else if (activeTab === 'posts') {
      loadPosts();
    }
  }, [activeTab]);

  const loadStats = async () => {
    try {
      const data = await adminApi.getStats();
      setStats(data);
      setLoading(false);
    } catch (err: any) {
      setError(err.message || '統計の読み込みに失敗しました。');
      setLoading(false);
    }
  };

  const loadUsers = async () => {
    try {
      const data = await adminApi.getAllUsers();
      setUsers(data);
    } catch (err: any) {
      alert(err.message || 'ユーザーリストの読み込みに失敗しました。');
    }
  };

  const loadPosts = async () => {
    try {
      const data = await adminApi.getAllPosts();
      setPosts(data);
    } catch (err: any) {
      alert(err.message || '投稿リストの読み込みに失敗しました。');
    }
  };

  const handleUserStatusChange = async (userId: number, newStatus: string) => {
    if (!window.confirm('ユーザーステータスを変更しますか？')) return;

    try {
      await adminApi.updateUserStatus(userId, newStatus);
      alert('ユーザーステータスが変更されました。');
      loadUsers();
    } catch (err: any) {
      alert(err.message || 'ステータス変更に失敗しました。');
    }
  };

  const handleDeletePost = async (postId: number) => {
    if (!window.confirm('投稿を削除しますか？')) return;

    try {
      await adminApi.deletePost(postId);
      alert('投稿が削除されました。');
      loadPosts();
    } catch (err: any) {
      alert(err.message || '投稿の削除に失敗しました。');
    }
  };

  // ユーザーフィルタリング
  const filteredUsers = users.filter(user => {
    const searchLower = userSearchTerm.toLowerCase();
    return (
      user.username?.toLowerCase().includes(searchLower) ||
      user.nickname?.toLowerCase().includes(searchLower) ||
      user.email?.toLowerCase().includes(searchLower) ||
      user.role?.toLowerCase().includes(searchLower) ||
      user.status?.toLowerCase().includes(searchLower)
    );
  });

  // 投稿フィルタリング
  const filteredPosts = posts.filter(post => {
    const searchLower = postSearchTerm.toLowerCase();
    return (
      post.title?.toLowerCase().includes(searchLower) ||
      post.author?.toLowerCase().includes(searchLower) ||
      post.boardName?.toLowerCase().includes(searchLower)
    );
  });

  if (loading) {
    return (
      <div className="admin-page">
        <div className="loading">読み込み中...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="admin-page">
        <div className="error-message">{error}</div>
      </div>
    );
  }

  return (
    <div className="admin-page">
      <div className="admin-header">
        <h1>管理者ページ</h1>
      </div>

      <div className="admin-tabs">
        <button
          className={`tab-button ${activeTab === 'dashboard' ? 'active' : ''}`}
          onClick={() => setActiveTab('dashboard')}
        >
          ダッシュボード
        </button>
        <button
          className={`tab-button ${activeTab === 'users' ? 'active' : ''}`}
          onClick={() => setActiveTab('users')}
        >
          ユーザー管理
        </button>
        <button
          className={`tab-button ${activeTab === 'posts' ? 'active' : ''}`}
          onClick={() => setActiveTab('posts')}
        >
          投稿管理
        </button>
      </div>

      <div className="admin-content">
        {activeTab === 'dashboard' && stats && (
          <div className="dashboard">
            <h2>システム統計</h2>
            <div className="stats-grid">
              <div className="stat-card">
                <div className="stat-icon">👥</div>
                <div className="stat-info">
                  <div className="stat-value">{stats.totalUsers}</div>
                  <div className="stat-label">総ユーザー数</div>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon">📝</div>
                <div className="stat-info">
                  <div className="stat-value">{stats.totalPosts}</div>
                  <div className="stat-label">総投稿数</div>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon">💬</div>
                <div className="stat-info">
                  <div className="stat-value">{stats.totalComments}</div>
                  <div className="stat-label">総コメント数</div>
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'users' && (
          <div className="users-management">
            <h2>ユーザー管理</h2>
            <div className="search-container">
              <input
                type="text"
                placeholder="ユーザー名、ニックネーム、メール、役割、ステータスで検索..."
                value={userSearchTerm}
                onChange={(e) => setUserSearchTerm(e.target.value)}
                className="search-input"
              />
              {userSearchTerm && (
                <button
                  onClick={() => setUserSearchTerm('')}
                  className="clear-search"
                >
                  ✕
                </button>
              )}
            </div>
            <div className="table-container">
              <table className="admin-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>ユーザー名</th>
                    <th>ニックネーム</th>
                    <th>メール</th>
                    <th>役割</th>
                    <th>ステータス</th>
                    <th>登録日</th>
                    <th>アクション</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredUsers.map((user) => (
                    <tr key={user.id}>
                      <td>{user.id}</td>
                      <td>{user.username}</td>
                      <td>{user.nickname}</td>
                      <td>{user.email}</td>
                      <td>
                        <span className={`badge ${user.role?.toLowerCase()}`}>
                          {user.role || 'USER'}
                        </span>
                      </td>
                      <td>
                        <span className={`badge ${user.status?.toLowerCase()}`}>
                          {user.status || 'ACTIVE'}
                        </span>
                      </td>
                      <td>{new Date(user.joinDate).toLocaleDateString('ko-KR')}</td>
                      <td>
                        <select
                          onChange={(e) => handleUserStatusChange(user.id, e.target.value)}
                          defaultValue=""
                          className="action-select"
                        >
                          <option value="" disabled>アクション選択</option>
                          <option value="ACTIVE">アクティブ化</option>
                          <option value="INACTIVE">非アクティブ化</option>
                          <option value="BANNED">ブロック</option>
                        </select>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {filteredUsers.length === 0 && (
                <p className="no-data">
                  {userSearchTerm ? '検索結果がありません。' : 'ユーザーがいません。'}
                </p>
              )}
            </div>
          </div>
        )}

        {activeTab === 'posts' && (
          <div className="posts-management">
            <h2>投稿管理</h2>
            <div className="search-container">
              <input
                type="text"
                placeholder="タイトル、作成者、掲示板で検索..."
                value={postSearchTerm}
                onChange={(e) => setPostSearchTerm(e.target.value)}
                className="search-input"
              />
              {postSearchTerm && (
                <button
                  onClick={() => setPostSearchTerm('')}
                  className="clear-search"
                >
                  ✕
                </button>
              )}
            </div>
            <div className="table-container">
              <table className="admin-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>タイトル</th>
                    <th>作成者</th>
                    <th>掲示板</th>
                    <th>閲覧数</th>
                    <th>作成日</th>
                    <th>アクション</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredPosts.map((post) => (
                    <tr key={post.id}>
                      <td>{post.id}</td>
                      <td className="title-cell">{post.title}</td>
                      <td>{post.author}</td>
                      <td>{post.boardName}</td>
                      <td>{post.viewCount}</td>
                      <td>{new Date(post.createdAt).toLocaleDateString('ko-KR')}</td>
                      <td>
                        <button
                          onClick={() => handleDeletePost(post.id)}
                          className="delete-btn"
                        >
                          削除
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {filteredPosts.length === 0 && (
                <p className="no-data">
                  {postSearchTerm ? '検索結果がありません。' : '投稿がありません。'}
                </p>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default AdminPage;
