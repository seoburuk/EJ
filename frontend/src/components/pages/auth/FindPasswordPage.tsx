import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './FindPasswordPage.scss';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const FindPasswordPage: React.FC = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState(1); // 1: メール/ユーザー名入力, 2: 認証コード入力およびパスワード変更
  const [formData, setFormData] = useState({
    email: '',
    username: '',
    verificationCode: '',
    newPassword: '',
    confirmPassword: '',
  });
  const [loading, setLoading] = useState(false);
  const [codeSent, setCodeSent] = useState(false);
  const [result, setResult] = useState<{ success: boolean; message: string } | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSendCode = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.email.trim() || !formData.username.trim()) {
      alert('メールアドレスとユーザー名を入力してください。');
      return;
    }

    setLoading(true);

    try {
      const response = await fetch(`${API_URL}/api/auth/send-verification-code`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
          email: formData.email,
          username: formData.username,
        }),
      });

      const data = await response.json();

      if (data.success) {
        alert(data.message);
        setCodeSent(true);
        setStep(2);
      } else {
        alert(data.message);
      }

    } catch (err) {
      console.error('認証コード送信エラー:', err);
      alert('認証コード送信中にエラーが発生しました。');
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.verificationCode.trim() || !formData.newPassword.trim() || !formData.confirmPassword.trim()) {
      alert('すべての項目を入力してください。');
      return;
    }

    if (formData.newPassword !== formData.confirmPassword) {
      alert('パスワードが一致しません。');
      return;
    }

    if (formData.newPassword.length < 6) {
      alert('パスワードは6文字以上である必要があります。');
      return;
    }

    setLoading(true);
    setResult(null);

    try {
      const response = await fetch(`${API_URL}/api/auth/reset-password`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
          email: formData.email,
          username: formData.username,
          verificationCode: formData.verificationCode,
          newPassword: formData.newPassword,
        }),
      });

      const data = await response.json();
      setResult(data);

      if (data.success) {
        setFormData({
          email: '',
          username: '',
          verificationCode: '',
          newPassword: '',
          confirmPassword: '',
        });
      }

    } catch (err) {
      console.error('パスワード再設定エラー:', err);
      setResult({
        success: false,
        message: 'パスワード再設定中にエラーが発生しました。',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="find-password-page">
      <div className="find-password-container">
        <h1>パスワード検索</h1>

        {step === 1 && !result && (
          <>
            <p className="description">メールアドレスとユーザー名を入力すると認証コードを送信します。</p>
            <form onSubmit={handleSendCode}>
              <div className="form-group">
                <label>メールアドレス</label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="example@email.com"
                  disabled={loading}
                />
              </div>

              <div className="form-group">
                <label>ユーザー名</label>
                <input
                  type="text"
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  placeholder="ユーザー名を入力してください"
                  disabled={loading}
                />
              </div>

              <button type="submit" className="submit-button" disabled={loading}>
                {loading ? '送信中...' : '認証コードを受け取る'}
              </button>
            </form>
          </>
        )}

        {step === 2 && !result && (
          <>
            <p className="description">メールアドレスに送信された認証コードを入力し、新しいパスワードを設定してください。</p>
            <div className="info-box">
              <p><strong>メールアドレス:</strong> {formData.email}</p>
              <p><strong>ユーザー名:</strong> {formData.username}</p>
            </div>
            <form onSubmit={handleResetPassword}>
              <div className="form-group">
                <label>認証コード</label>
                <input
                  type="text"
                  name="verificationCode"
                  value={formData.verificationCode}
                  onChange={handleChange}
                  placeholder="6桁の数字を入力"
                  disabled={loading}
                  maxLength={6}
                />
              </div>

              <div className="form-group">
                <label>新しいパスワード</label>
                <input
                  type="password"
                  name="newPassword"
                  value={formData.newPassword}
                  onChange={handleChange}
                  placeholder="6文字以上を入力してください"
                  disabled={loading}
                />
              </div>

              <div className="form-group">
                <label>パスワード確認</label>
                <input
                  type="password"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  placeholder="パスワードを再度入力してください"
                  disabled={loading}
                />
              </div>

              <div className="button-group">
                <button
                  type="button"
                  className="back-button"
                  onClick={() => {
                    setStep(1);
                    setCodeSent(false);
                  }}
                  disabled={loading}
                >
                  前へ
                </button>
                <button type="submit" className="submit-button" disabled={loading}>
                  {loading ? '処理中...' : 'パスワード変更'}
                </button>
              </div>
            </form>
          </>
        )}

        {result && (
          <div className={`result ${result.success ? 'success' : 'error'}`}>
            <div className="result-icon">
              {result.success ? '✓' : '✗'}
            </div>
            <p className="result-message">{result.message}</p>
            <div className="result-buttons">
              {result.success ? (
                <button onClick={() => navigate('/login')} className="login-button">
                  ログイン
                </button>
              ) : (
                <button onClick={() => {
                  setResult(null);
                  setStep(1);
                  setCodeSent(false);
                }} className="retry-button">
                  再試行
                </button>
              )}
            </div>
          </div>
        )}

        <div className="links">
          <button onClick={() => navigate('/login')}>ログイン</button>
          <span>|</span>
          <button onClick={() => navigate('/find-username')}>ユーザー名検索</button>
          <span>|</span>
          <button onClick={() => navigate('/register')}>会員登録</button>
        </div>
      </div>
    </div>
  );
};

export default FindPasswordPage;
