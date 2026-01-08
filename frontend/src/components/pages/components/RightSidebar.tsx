import React from 'react';
import { useNavigate } from 'react-router-dom';
import { HotPost } from '../../../types';
import './RightSidebar.scss';

interface RightSidebarProps {
  hotPosts: HotPost[];
}

const RightSidebar: React.FC<RightSidebarProps> = ({ hotPosts }) => {
  const navigate = useNavigate();

  const handlePostClick = (postId: number) => {
    navigate(`/posts/${postId}`);
  };

  return (
    <div className="right-sidebar">
      <div className="sidebar-section">
        <h3 className="section-title">リアルタイム人気投稿</h3>
        <div className="hot-posts-list">
          {hotPosts.length > 0 ? (
            hotPosts.map((post, index) => (
              <div
                key={post.id}
                className="hot-post-item"
                onClick={() => handlePostClick(post.id)}
              >
                <div className="post-rank">{index + 1}</div>
                <div className="post-content">
                  <div className="post-title">{post.title}</div>
                  <div className="post-meta">
                    <span className="author">{post.author}</span>
                    <div className="post-stats">
                      <span className="stat">
                        <span className="icon">💬</span>
                        {post.commentCount}
                      </span>
                      <span className="stat">
                        <span className="icon">❤️</span>
                        {post.likeCount}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className="no-content">人気投稿がありません。</div>
          )}
        </div>
      </div>
    </div>
  );
};

export default RightSidebar;
