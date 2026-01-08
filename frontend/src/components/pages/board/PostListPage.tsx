import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { postApi, Post } from '../../../api/post';
import './PostListPage.scss';

const BOARD_NAMES: { [key: number]: string } = {
  1: '中古マーケット',
  2: '自由掲示板',
  3: 'イベント掲示板',
  4: '質問掲示板',
  5: '人気投稿',
};

const PostListPage: React.FC = () => {
  const { boardId } = useParams<{ boardId: string }>();
  const navigate = useNavigate();
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const currentBoardId = Number(boardId);
  const boardName = BOARD_NAMES[currentBoardId] || '掲示板';

  useEffect(() => {
    loadPosts();
  }, [boardId]);

  const loadPosts = async () => {
    if (!boardId) return;

    try {
      const data = await postApi.getPostsByBoard(Number(boardId));
      setPosts(data);
    } catch (err) {
      setError('投稿の読み込み中にエラーが発生しました。');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handlePostClick = (postId: number) => {
    navigate(`/posts/${postId}`);
  };

  const handleWriteClick = () => {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      alert('ログインが必要です。');
      navigate('/login');
      return;
    }
    navigate(`/posts/create?boardId=${currentBoardId}`);
  };

  const handleAuthorClick = (e: React.MouseEvent, authorId: number) => {
    e.stopPropagation(); // 投稿クリックイベント防止
    navigate(`/profile/${authorId}`);
  };

  const getTimeAgo = (timestamp: string | number): string => {
    const now = new Date();
    const past = new Date(timestamp);
    const diffInSeconds = Math.floor((now.getTime() - past.getTime()) / 1000);

    if (diffInSeconds < 60) {
      return 'たった今';
    } else if (diffInSeconds < 3600) {
      const minutes = Math.floor(diffInSeconds / 60);
      return `${minutes}分前`;
    } else if (diffInSeconds < 86400) {
      const hours = Math.floor(diffInSeconds / 3600);
      return `${hours}時間前`;
    } else if (diffInSeconds < 604800) {
      const days = Math.floor(diffInSeconds / 86400);
      return `${days}日前`;
    } else {
      return past.toLocaleDateString('ja-JP', { month: 'short', day: 'numeric' });
    }
  };

  if (loading) return <div className="loading">読み込み中...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="post-list-page">
      <div className="post-list-container">
        <div className="board-header">
          <h1 className="board-title">{boardName}</h1>
          <button className="write-button" onClick={handleWriteClick}>
            投稿作成
          </button>
        </div>

        {posts.length === 0 ? (
          <div className="no-posts">
            <p>まだ投稿がありません。</p>
            <p>最初の投稿を作成してみましょう！</p>
          </div>
        ) : (
          <div className="post-list">
            <div className="post-list-header">
              <div className="col-title">タイトル</div>
              <div className="col-author">作成者</div>
              <div className="col-views">閲覧数</div>
              <div className="col-date">作成時間</div>
            </div>

            {posts.map((post) => (
              <div
                key={post.id}
                className="post-item"
                onClick={() => handlePostClick(post.id)}
              >
                <div className="col-title">
                  <span className="title-text">{post.title}</span>
                  {post.commentCount > 0 && (
                    <span className="comment-count">[{post.commentCount}]</span>
                  )}
                </div>
                <div
                  className="col-author author-link"
                  onClick={(e) => handleAuthorClick(e, post.authorId)}
                >
                  {post.author}
                </div>
                <div className="col-views">{post.viewCount}</div>
                <div className="col-date">{getTimeAgo(post.createdAt)}</div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default PostListPage;
