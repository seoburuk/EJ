import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { commentApi, Comment } from '../../../api/comment';
import './CommentSection.scss';

interface CommentSectionProps {
  postId: number;
}

const CommentSection: React.FC<CommentSectionProps> = ({ postId }) => {
  const navigate = useNavigate();
  const [comments, setComments] = useState<Comment[]>([]);
  const [newComment, setNewComment] = useState('');
  const [replyTo, setReplyTo] = useState<number | null>(null);
  const [replyContent, setReplyContent] = useState('');
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editContent, setEditContent] = useState('');
  const [isAnonymous, setIsAnonymous] = useState(false);
  const [likedComments, setLikedComments] = useState<Set<number>>(new Set());

  const currentUserId = localStorage.getItem('userId');

  useEffect(() => {
    loadComments();
  }, [postId]);

  const loadComments = async () => {
    try {
      const data = await commentApi.getCommentsByPost(postId);
      setComments(data);

      // ログイン時にいいね状態読み込み
      if (currentUserId) {
        const likedSet = new Set<number>();
        for (const comment of data) {
          try {
            const isLiked = await commentApi.isCommentLiked(comment.id);
            if (isLiked) {
              likedSet.add(comment.id);
            }
          } catch (err) {
            console.error(`コメント ${comment.id} いいね状態確認失敗:`, err);
          }
        }
        setLikedComments(likedSet);
      }
    } catch (err) {
      console.error('コメント読み込み失敗:', err);
    }
  };

  const handleSubmitComment = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!newComment.trim()) {
      alert('コメント内容を入力してください。');
      return;
    }

    try {
      const response = await commentApi.createComment({
        postId,
        content: newComment,
        isAnonymous,
      });

      if (response.success) {
        setNewComment('');
        setIsAnonymous(false);
        loadComments();
      } else {
        alert(response.message);
      }
    } catch (err) {
      alert('コメント作成中にエラーが発生しました。');
      console.error(err);
    }
  };

  const handleSubmitReply = async (parentId: number) => {
    if (!replyContent.trim()) {
      alert('返信内容を入力してください。');
      return;
    }

    try {
      const response = await commentApi.createComment({
        postId,
        parentId,
        content: replyContent,
        isAnonymous,
      });

      if (response.success) {
        setReplyTo(null);
        setReplyContent('');
        setIsAnonymous(false);
        loadComments();
      } else {
        alert(response.message);
      }
    } catch (err) {
      alert('返信作成中にエラーが発生しました。');
      console.error(err);
    }
  };

  const handleUpdateComment = async (commentId: number) => {
    if (!editContent.trim()) {
      alert('コメント内容を入力してください。');
      return;
    }

    try {
      const response = await commentApi.updateComment(commentId, {
        content: editContent,
      });

      if (response.success) {
        setEditingId(null);
        setEditContent('');
        loadComments();
      } else {
        alert(response.message);
      }
    } catch (err) {
      alert('コメント編集中にエラーが発生しました。');
      console.error(err);
    }
  };

  const handleDeleteComment = async (commentId: number) => {
    if (!window.confirm('コメントを削除しますか？')) return;

    try {
      const response = await commentApi.deleteComment(commentId);

      if (response.success) {
        loadComments();
      } else {
        alert(response.message);
      }
    } catch (err) {
      alert('コメント削除中にエラーが発生しました。');
      console.error(err);
    }
  };

  const handleLike = async (commentId: number) => {
    if (!currentUserId) {
      alert('ログインが必要です。');
      return;
    }

    try {
      const response = await commentApi.toggleCommentLike(commentId);

      if (response.success) {
        // ローカル状態更新
        const newLikedComments = new Set(likedComments);
        if (response.isLiked) {
          newLikedComments.add(commentId);
        } else {
          newLikedComments.delete(commentId);
        }
        setLikedComments(newLikedComments);

        // コメントリスト再読み込み（いいね数更新のため）
        loadComments();
      } else {
        alert(response.message);
      }
    } catch (err) {
      alert('いいね処理中にエラーが発生しました。');
      console.error(err);
    }
  };

  const startEdit = (comment: Comment) => {
    setEditingId(comment.id);
    setEditContent(comment.content);
  };

  const cancelEdit = () => {
    setEditingId(null);
    setEditContent('');
  };

  const startReply = (commentId: number) => {
    setReplyTo(commentId);
    setReplyContent('');
  };

  const cancelReply = () => {
    setReplyTo(null);
    setReplyContent('');
  };

  // 댓글과 대댓글 분리
  const topLevelComments = comments.filter(c => !c.parentId);
  const getReplies = (parentId: number) => comments.filter(c => c.parentId === parentId);

  const isAuthor = (comment: Comment) => {
    return currentUserId && comment.authorId.toString() === currentUserId;
  };

  const handleAuthorClick = (comment: Comment) => {
    // 匿名コメントはプロフィールに移動しない
    if (comment.isAnonymous) {
      return;
    }
    navigate(`/profile/${comment.authorId}`);
  };

  return (
    <div className="comment-section">
      <h3 className="comment-title">コメント {comments.length}</h3>

      {/* 新規コメント作成 */}
      <form onSubmit={handleSubmitComment} className="comment-form">
        <textarea
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
          placeholder="コメントを入力してください"
          rows={3}
        />
        <div className="comment-form-actions">
          <label className="anonymous-checkbox">
            <input
              type="checkbox"
              checked={isAnonymous}
              onChange={(e) => setIsAnonymous(e.target.checked)}
            />
            匿名
          </label>
          <button type="submit" className="submit-button">
            コメント作成
          </button>
        </div>
      </form>

      {/* コメントリスト */}
      <div className="comment-list">
        {topLevelComments.map((comment) => (
          <div key={comment.id} className="comment-item">
            <div className="comment-header">
              <span
                className={`comment-author ${comment.isAnonymous ? '' : 'author-link'}`}
                onClick={() => handleAuthorClick(comment)}
                style={{ cursor: comment.isAnonymous ? 'default' : 'pointer' }}
              >
                {comment.author}
              </span>
              <span className="comment-date">
                {new Date(comment.createdAt).toLocaleString('ja-JP')}
              </span>
            </div>

            {editingId === comment.id ? (
              <div className="comment-edit-form">
                <textarea
                  value={editContent}
                  onChange={(e) => setEditContent(e.target.value)}
                  rows={3}
                />
                <div className="edit-actions">
                  <button onClick={() => handleUpdateComment(comment.id)} className="save-button">
                    保存
                  </button>
                  <button onClick={cancelEdit} className="cancel-button">
                    キャンセル
                  </button>
                </div>
              </div>
            ) : (
              <>
                <div className="comment-content">{comment.content}</div>
                <div className="comment-actions">
                  <button
                    onClick={() => handleLike(comment.id)}
                    className={`like-button ${likedComments.has(comment.id) ? 'liked' : ''}`}
                  >
                    ♥ {comment.likeCount || 0}
                  </button>
                  <button onClick={() => startReply(comment.id)} className="reply-button">
                    返信
                  </button>
                  {isAuthor(comment) && (
                    <>
                      <button onClick={() => startEdit(comment)} className="edit-button">
                        編集
                      </button>
                      <button onClick={() => handleDeleteComment(comment.id)} className="delete-button">
                        削除
                      </button>
                    </>
                  )}
                </div>
              </>
            )}

            {/* 返信作成フォーム */}
            {replyTo === comment.id && (
              <div className="reply-form">
                <textarea
                  value={replyContent}
                  onChange={(e) => setReplyContent(e.target.value)}
                  placeholder="返信を入力してください"
                  rows={2}
                />
                <div className="reply-form-actions">
                  <label className="anonymous-checkbox">
                    <input
                      type="checkbox"
                      checked={isAnonymous}
                      onChange={(e) => setIsAnonymous(e.target.checked)}
                    />
                    匿名
                  </label>
                  <button onClick={() => handleSubmitReply(comment.id)} className="submit-button">
                    返信作成
                  </button>
                  <button onClick={cancelReply} className="cancel-button">
                    キャンセル
                  </button>
                </div>
              </div>
            )}

            {/* 返信リスト */}
            {getReplies(comment.id).map((reply) => (
              <div key={reply.id} className="comment-item reply">
                <div className="comment-header">
                  <span
                    className={`comment-author ${reply.isAnonymous ? '' : 'author-link'}`}
                    onClick={() => handleAuthorClick(reply)}
                    style={{ cursor: reply.isAnonymous ? 'default' : 'pointer' }}
                  >
                    {reply.author}
                  </span>
                  <span className="comment-date">
                    {new Date(reply.createdAt).toLocaleString('ja-JP')}
                  </span>
                </div>

                {editingId === reply.id ? (
                  <div className="comment-edit-form">
                    <textarea
                      value={editContent}
                      onChange={(e) => setEditContent(e.target.value)}
                      rows={2}
                    />
                    <div className="edit-actions">
                      <button onClick={() => handleUpdateComment(reply.id)} className="save-button">
                        保存
                      </button>
                      <button onClick={cancelEdit} className="cancel-button">
                        キャンセル
                      </button>
                    </div>
                  </div>
                ) : (
                  <>
                    <div className="comment-content">{reply.content}</div>
                    <div className="comment-actions">
                      <button
                        onClick={() => handleLike(reply.id)}
                        className={`like-button ${likedComments.has(reply.id) ? 'liked' : ''}`}
                      >
                        ♥ {reply.likeCount || 0}
                      </button>
                      {isAuthor(reply) && (
                        <>
                          <button onClick={() => startEdit(reply)} className="edit-button">
                            編集
                          </button>
                          <button onClick={() => handleDeleteComment(reply.id)} className="delete-button">
                            削除
                          </button>
                        </>
                      )}
                    </div>
                  </>
                )}
              </div>
            ))}
          </div>
        ))}
      </div>
    </div>
  );
};

export default CommentSection;
