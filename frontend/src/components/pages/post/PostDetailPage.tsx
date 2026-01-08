import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { postApi, Post } from '../../../api/post';
import { messageApi } from '../../../api/message';
import { reportApi } from '../../../api/report';
import CommentSection from './CommentSection';
import './PostDetailPage.scss';

const PostDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [post, setPost] = useState<Post | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isLiked, setIsLiked] = useState(false);
  const [likeCount, setLikeCount] = useState(0);
  const [showMessageModal, setShowMessageModal] = useState(false);
  const [messageTitle, setMessageTitle] = useState('');
  const [messageContent, setMessageContent] = useState('');

  const currentUserId = localStorage.getItem('userId');

  useEffect(() => {
    loadPost();
    loadLikeInfo();
  }, [id]);

  const loadPost = async () => {
    if (!id) return;

    try {
      const data = await postApi.getPost(Number(id));
      setPost(data);
    } catch (err) {
      setError('投稿の読み込み中にエラーが発生しました。');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const loadLikeInfo = async () => {
    if (!id) return;

    try {
      const [count, status] = await Promise.all([
        postApi.getLikeCount(Number(id)),
        postApi.getLikeStatus(Number(id))
      ]);
      setLikeCount(count);
      setIsLiked(status);
    } catch (err) {
      console.error('いいね情報読み込み失敗:', err);
    }
  };

  const handleDelete = async () => {
    if (!window.confirm('本当に削除しますか？')) return;
    if (!id) return;

    try {
      const response = await postApi.deletePost(Number(id));
      if (response.success) {
        alert(response.message);
        navigate(`/board/${post?.boardId}`);
      } else {
        alert(response.message);
      }
    } catch (err) {
      alert('削除中にエラーが発生しました。');
      console.error(err);
    }
  };

  const handleLike = async () => {
    if (!currentUserId) {
      alert('ログインが必要です。');
      return;
    }

    if (!id) return;

    try {
      console.log('いいねリクエスト開始:', id);
      const response = await postApi.toggleLike(Number(id));
      console.log('いいねレスポンス:', response);

      if (response.success) {
        // いいね状態反転
        setIsLiked(!isLiked);
        setLikeCount(prev => isLiked ? prev - 1 : prev + 1);
      } else {
        alert(response.message || 'いいね処理に失敗しました。');
      }
    } catch (err: any) {
      console.error('いいねエラー:', err);
      alert(err.message || 'いいね処理中にエラーが発生しました。');
    }
  };

  const handleAuthorClick = () => {
    if (post) {
      navigate(`/profile/${post.authorId}`);
    }
  };

  const handleSendMessage = async () => {
    if (!currentUserId) {
      alert('ログインが必要です。');
      return;
    }

    if (!post) return;

    if (!messageTitle.trim() || !messageContent.trim()) {
      alert('タイトルと内容を入力してください。');
      return;
    }

    try {
      await messageApi.sendMessage(post.authorId, messageTitle, messageContent);
      alert('メッセージが送信されました。');
      setShowMessageModal(false);
      setMessageTitle('');
      setMessageContent('');
    } catch (err: any) {
      alert(err.message || 'メッセージ送信に失敗しました。');
    }
  };

  const handleReport = async () => {
    if (!currentUserId) {
      alert('ログインが必要です。');
      return;
    }

    if (!id) return;

    const reason = prompt('通報理由を入力してください:');
    if (reason && reason.trim()) {
      try {
        const response = await reportApi.reportPost(Number(id), reason.trim());
        if (response.success) {
          alert('通報が受理されました。\n管理者が確認後に対応いたします。');
        } else {
          alert(response.message || '通報受理に失敗しました。');
        }
      } catch (err: any) {
        console.error('通報エラー:', err);
        alert(err.message || '通報受理中にエラーが発生しました。');
      }
    }
  };

  if (loading) return <div className="loading">読み込み中...</div>;
  if (error) return <div className="error">{error}</div>;
  if (!post) return <div className="error">投稿が見つかりません。</div>;

  const isAuthor = currentUserId && post.authorId.toString() === currentUserId;

  return (
    <div className="post-detail-page">
      <div className="post-detail-container">
        <div className="post-header">
          <div className="board-name">{post.boardName}</div>
          <h1 className="post-title">{post.title}</h1>
          <div className="post-meta">
            <span className="author author-link" onClick={handleAuthorClick}>
              {post.author}
            </span>
            <span className="separator">|</span>
            <span className="date">{new Date(post.createdAt).toLocaleString('ja-JP')}</span>
            <span className="separator">|</span>
            <span className="views">閲覧数 {post.viewCount}</span>
          </div>
          <div className="post-actions-top">
            {currentUserId && currentUserId !== post.authorId.toString() && (
              <>
                <button className="message-btn" onClick={() => setShowMessageModal(true)}>
                  ✉️ メッセージ送信
                </button>
                <button className="report-btn" onClick={handleReport}>
                  🚨 通報
                </button>
              </>
            )}
          </div>
        </div>

        <div className="post-content">
          {post.content.split('\n').map((line, index) => (
            <p key={index}>{line || '\u00A0'}</p>
          ))}
        </div>

        {/* 画像表示 */}
        {post.images && post.images.length > 0 && (
          <div className="post-images">
            {post.images.map((image: any, index: number) => (
              <div key={image.id || index} className="post-image-item">
                <img
                  src={`${process.env.REACT_APP_API_URL || 'http://localhost:8080'}${image.imageUrl}`}
                  alt={`投稿画像 ${index + 1}`}
                  onError={(e) => {
                    console.error('画像読み込み失敗:', image.imageUrl);
                    (e.target as HTMLImageElement).style.display = 'none';
                  }}
                />
              </div>
            ))}
          </div>
        )}

        <div className="post-stats">
          <button
            className={`like-button ${isLiked ? 'liked' : ''}`}
            onClick={handleLike}
          >
            <span className="icon">{isLiked ? '❤️' : '🤍'}</span>
            <span className="count">{likeCount}</span>
          </button>
          <div className="stat">
            <span className="icon">💬</span>
            <span className="count">{post.commentCount}</span>
          </div>
        </div>

        <div className="button-group">
          <button className="list-button" onClick={() => navigate(`/board/${post.boardId}`)}>
            リストへ
          </button>
          {isAuthor && (
            <>
              <button className="edit-button" onClick={() => navigate(`/posts/${post.id}/edit`)}>
                編集
              </button>
              <button className="delete-button" onClick={handleDelete}>
                削除
              </button>
            </>
          )}
        </div>

        {/* コメントセクション */}
        <CommentSection postId={post.id} />
      </div>

      {/* メッセージ送信モーダル */}
      {showMessageModal && (
        <div className="modal-overlay" onClick={() => setShowMessageModal(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <h2>メッセージ送信</h2>
            <p className="recipient">宛先: {post.author}</p>
            <div className="form-group">
              <label>タイトル</label>
              <input
                type="text"
                value={messageTitle}
                onChange={(e) => setMessageTitle(e.target.value)}
                placeholder="タイトルを入力してください"
              />
            </div>
            <div className="form-group">
              <label>内容</label>
              <textarea
                value={messageContent}
                onChange={(e) => setMessageContent(e.target.value)}
                placeholder="内容を入力してください"
                rows={10}
              />
            </div>
            <div className="modal-buttons">
              <button className="send-button" onClick={handleSendMessage}>
                送信
              </button>
              <button className="cancel-button" onClick={() => setShowMessageModal(false)}>
                キャンセル
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default PostDetailPage;
