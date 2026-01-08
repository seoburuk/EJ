import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './FindUsernamePage.scss';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const FindUsernamePage: React.FC = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState<{ success: boolean; message: string; username?: string } | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!email.trim()) {
      alert('メールアドレスを入力してください。');
      return;
    }

    setLoading(true);
    setResult(null);

    try {
      const response = await fetch(`${API_URL}/api/auth/find-username`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({ email }),
      });

      const data = await response.json();
      setResult(data);

    } catch (err) {
      console.error('ユーザー名検索エラー:', err);
      setResult({
        success: false,
        message: 'ユーザー名検索中にエラーが発生しました。',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="find-username-page">
      <div className="find-username-container">
        <h1>ユーザー名検索</h1>
        <p className="description">登録時に入力したメールアドレスを入力してください。</p>

        {!result && (
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>メールアドレス</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="example@email.com"
                disabled={loading}
              />
            </div>

            <button type="submit" className="submit-button" disabled={loading}>
              {loading ? '検索中...' : 'ユーザー名検索'}
            </button>
          </form>
        )}

        {result && (
          <div className={`result ${result.success ? 'success' : 'error'}`}>
            <div className="result-icon">
              {result.success ? '✓' : '✗'}
            </div>
            <p className="result-message">{result.message}</p>
            {result.success && result.username && (
              <div className="username-box">
                <strong>ユーザー名:</strong> {result.username}
              </div>
            )}
            <div className="result-buttons">
              {result.success ? (
                <>
                  <button onClick={() => navigate('/login')} className="login-button">
                    ログイン
                  </button>
                  <button onClick={() => navigate('/find-password')} className="find-password-button">
                    パスワード検索
                  </button>
                </>
              ) : (
                <button onClick={() => setResult(null)} className="retry-button">
                  再試行
                </button>
              )}
            </div>
          </div>
        )}

        <div className="links">
          <button onClick={() => navigate('/login')}>ログイン</button>
          <span>|</span>
          <button onClick={() => navigate('/find-password')}>パスワード検索</button>
          <span>|</span>
          <button onClick={() => navigate('/register')}>会員登録</button>
        </div>
      </div>
    </div>
  );
};

export default FindUsernamePage;
