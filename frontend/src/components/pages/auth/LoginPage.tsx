import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authApi, LoginRequest } from '../../../api/auth';
import './LoginPage.scss';

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<LoginRequest>({
    username: '',
    password: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await authApi.login(formData);

      if (response.success) {
        // ログイン成功時にユーザー情報を保存 (localStorage または状態管理)
        if (response.userId) {
          localStorage.setItem('userId', response.userId.toString());
          localStorage.setItem('username', response.username || '');
          localStorage.setItem('nickname', response.nickname || '');

          alert(response.message);
          // ページ全体のリロードでヘッダー状態を更新
          window.location.href = '/';
        } else {
          setError('ログイン情報を取得できませんでした。もう一度お試しください。');
        }
      } else {
        setError(response.message);
      }
    } catch (err) {
      setError('サーバーとの通信中にエラーが発生しました。');
      console.error('ログインエラー:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <h1 className="login-title">ログイン</h1>
        <p className="login-subtitle">Globalinへようこそ</p>

        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="username">ユーザー名</label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="ユーザー名を入力してください"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">パスワード</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="パスワードを入力してください"
              required
            />
          </div>

          {error && <div className="error-message">{error}</div>}

          <button type="submit" className="login-button" disabled={loading}>
            {loading ? '処理中...' : 'ログイン'}
          </button>
        </form>

        <div className="auth-links">
          <a href="/find-username" onClick={(e) => { e.preventDefault(); navigate('/find-username'); }}>
            ユーザー名を探す
          </a>
          <span className="divider">|</span>
          <a href="/find-password" onClick={(e) => { e.preventDefault(); navigate('/find-password'); }}>
            パスワードを探す
          </a>
          <span className="divider">|</span>
          <a href="/register" onClick={(e) => { e.preventDefault(); navigate('/register'); }}>
            会員登録
          </a>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
