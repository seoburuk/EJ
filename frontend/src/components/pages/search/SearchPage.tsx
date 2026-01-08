import React, { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { postApi, Post } from '../../../api/post';
import './SearchPage.scss';

const SearchPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const query = searchParams.get('q') || '';

  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (query) {
      searchPosts();
    }
  }, [query]);

  const searchPosts = async () => {
    setLoading(true);
    setError('');

    try {
      const results = await postApi.searchPosts(query);
      setPosts(results);
    } catch (err) {
      console.error('検索エラー:', err);
      setError('検索中にエラーが発生しました。');
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (timestamp: number) => {
    const date = new Date(timestamp);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffMins < 1) return 'たった今';
    if (diffMins < 60) return `${diffMins}分前`;
    if (diffHours < 24) return `${diffHours}時間前`;
    if (diffDays < 7) return `${diffDays}日前`;

    return date.toLocaleDateString('ko-KR');
  };

  const handlePostClick = (postId: number) => {
    navigate(`/posts/${postId}`);
  };

  if (loading) {
    return (
      <div className="search-page">
        <div className="loading">検索中...</div>
      </div>
    );
  }

  return (
    <div className="search-page">
      <div className="search-container">
        <div className="search-header">
          <h1>検索結果</h1>
          <p className="search-query">"{query}"の検索結果 ({posts.length}件)</p>
        </div>

        {error && <div className="error-message">{error}</div>}

        {posts.length === 0 ? (
          <div className="no-results">
            <div className="no-results-icon">🔍</div>
            <h2>検索結果がありません</h2>
            <p>他のキーワードで検索してください。</p>
          </div>
        ) : (
          <div className="search-results">
            {posts.map((post) => (
              <div
                key={post.id}
                className="post-card"
                onClick={() => handlePostClick(post.id)}
              >
                <div className="post-header">
                  <span className="board-name">{post.boardName}</span>
                  <span className="post-date">{formatDate(post.createdAt)}</span>
                </div>

                <h3 className="post-title">{post.title}</h3>

                <div className="post-preview">
                  {post.content.substring(0, 150)}
                  {post.content.length > 150 && '...'}
                </div>

                <div className="post-meta">
                  <span className="author">{post.author}</span>
                  <span className="separator">•</span>
                  <span className="stat">👁️ {post.viewCount}</span>
                  <span className="separator">•</span>
                  <span className="stat">❤️ {post.likeCount}</span>
                  <span className="separator">•</span>
                  <span className="stat">💬 {post.commentCount}</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default SearchPage;
