import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authApi, RegisterRequest } from '../../../api/auth';
import './RegisterPage.scss';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const RegisterPage: React.FC = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState(1); // 1: メール認証, 2: 会員登録完了
  const [formData, setFormData] = useState<RegisterRequest>({
    username: '',
    password: '',
    email: '',
    nickname: '',
  });
  const [confirmPassword, setConfirmPassword] = useState('');
  const [verificationCode, setVerificationCode] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [codeSent, setCodeSent] = useState(false);
  const [displayCode, setDisplayCode] = useState(''); // テスト用：認証コードを表示

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // 1段階: メール認証コード送信
  const handleSendCode = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!formData.email.trim()) {
      setError('メールアドレスを入力してください。');
      return;
    }

    setLoading(true);

    try {
      const response = await fetch(`${API_URL}/api/auth/send-register-verification-code`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
          email: formData.email,
        }),
      });

      const data = await response.json();

      if (data.success) {
        alert(data.message);
        setCodeSent(true);
        setDisplayCode(data.verificationCode || ''); // テスト用：認証コードを保存
        setStep(2);
      } else {
        setError(data.message);
      }
    } catch (err) {
      console.error('認証コード送信エラー:', err);
      setError('認証コード送信中にエラーが発生しました。');
    } finally {
      setLoading(false);
    }
  };

  // 2段階: 会員登録完了
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    // クライアント側バリデーション
    if (!verificationCode.trim()) {
      setError('認証コードを入力してください。');
      return;
    }

    if (formData.password !== confirmPassword) {
      setError('パスワードが一致しません。');
      return;
    }

    if (formData.username.length < 4 || formData.username.length > 20) {
      setError('ユーザー名は4〜20文字である必要があります。');
      return;
    }

    if (formData.password.length < 8) {
      setError('パスワードは8文字以上である必要があります。');
      return;
    }

    setLoading(true);

    try {
      const response = await fetch(`${API_URL}/api/auth/register-with-verification`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
          ...formData,
          verificationCode: verificationCode,
        }),
      });

      const data = await response.json();

      if (data.success) {
        alert(data.message);
        navigate('/login');
      } else {
        setError(data.message);
      }
    } catch (err) {
      setError('サーバーとの通信中にエラーが発生しました。');
      console.error('会員登録エラー:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="register-page">
      <div className="register-container">
        <h1 className="register-title">会員登録</h1>
        <p className="register-subtitle">Globalinへようこそ</p>

        {step === 1 && (
          <>
            <p className="description">メールアドレスを入力して認証コードを受け取ってください。</p>
            <form onSubmit={handleSendCode} className="register-form">
              <div className="form-group">
                <label htmlFor="email">メールアドレス</label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="example@example.com"
                  required
                  disabled={loading}
                />
              </div>

              {error && <div className="error-message">{error}</div>}

              <button type="submit" className="register-button" disabled={loading}>
                {loading ? '送信中...' : '認証コードを受け取る'}
              </button>
            </form>
          </>
        )}

        {step === 2 && (
          <>
            <p className="description">メールで送信された認証コードを入力し、会員情報を記入してください。</p>
            <div className="info-box">
              <p><strong>メールアドレス:</strong> {formData.email}</p>
              {displayCode && (
                <p style={{ color: '#e74c3c', fontWeight: 'bold', marginTop: '10px' }}>
                  <strong>テスト用認証コード:</strong> {displayCode}
                </p>
              )}
            </div>
            <form onSubmit={handleSubmit} className="register-form">
              <div className="form-group">
                <label htmlFor="verificationCode">認証コード</label>
                <input
                  type="text"
                  id="verificationCode"
                  name="verificationCode"
                  value={verificationCode}
                  onChange={(e) => setVerificationCode(e.target.value)}
                  placeholder="6桁の数字を入力"
                  required
                  maxLength={6}
                  disabled={loading}
                />
              </div>

              <div className="form-group">
                <label htmlFor="username">ユーザー名</label>
                <input
                  type="text"
                  id="username"
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  placeholder="4〜20文字の英数字"
                  required
                  minLength={4}
                  maxLength={20}
                  disabled={loading}
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
                  placeholder="最低8文字以上"
                  required
                  minLength={8}
                  disabled={loading}
                />
              </div>

              <div className="form-group">
                <label htmlFor="confirmPassword">パスワード確認</label>
                <input
                  type="password"
                  id="confirmPassword"
                  name="confirmPassword"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  placeholder="パスワードを再入力してください"
                  required
                  disabled={loading}
                />
              </div>

              <div className="form-group">
                <label htmlFor="nickname">ニックネーム</label>
                <input
                  type="text"
                  id="nickname"
                  name="nickname"
                  value={formData.nickname}
                  onChange={handleChange}
                  placeholder="使用するニックネームを入力してください"
                  required
                  maxLength={50}
                  disabled={loading}
                />
              </div>

              {error && <div className="error-message">{error}</div>}

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
                  戻る
                </button>
                <button type="submit" className="register-button" disabled={loading}>
                  {loading ? '処理中...' : '会員登録'}
                </button>
              </div>
            </form>
          </>
        )}

        <div className="login-link">
          すでにアカウントをお持ちですか？{' '}
          <a href="/login" onClick={(e) => { e.preventDefault(); navigate('/login'); }}>
            ログイン
          </a>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;
