import React from 'react';
import { Post } from '../../../types';
import { useNavigate } from 'react-router-dom';
import './MainContent.scss';

interface BoardWithPosts {
  id: number;
  name: string;
  description: string;
  category: string;
  icon: string;
  postCount: number;
  recentPosts: Post[];
}

interface MainContentProps {
  humanitiesPosts: Post[];
  freePosts: Post[];
  allBoards?: BoardWithPosts[];
}

const MainContent: React.FC<MainContentProps> = ({ humanitiesPosts, freePosts, allBoards }) => {
  const navigate = useNavigate();

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

  const handleAuthorClick = (e: React.MouseEvent, authorId: number) => {
    e.stopPropagation();
    navigate(`/profile/${authorId}`);
  };

  const renderPostList = (posts: Post[], title: string) => (
    <div className="post-section">
      <h2 className="section-title">{title}</h2>
      <div className="post-list">
        <div className="post-list-header">
          <div className="col-title">タイトル</div>
          <div className="col-author">作成者</div>
          <div className="col-views">閲覧数</div>
          <div className="col-date">作成時間</div>
        </div>
        {posts.length > 0 ? (
          posts.map((post) => (
            <div
              key={post.id}
              className="post-item"
              onClick={() => navigate(`/posts/${post.id}`)}
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
          ))
        ) : (
          <div className="no-posts">投稿がありません。</div>
        )}
      </div>
    </div>
  );

  return (
    <div className="main-content">
      {allBoards && allBoards.length > 0 ? (
        allBoards.map((board) => (
          <div key={board.id}>
            {renderPostList(board.recentPosts, board.name)}
          </div>
        ))
      ) : (
        <>
          {renderPostList(humanitiesPosts, '中古マーケット')}
          {renderPostList(freePosts, '自由掲示板')}
        </>
      )}
    </div>
  );
};

export default MainContent;
