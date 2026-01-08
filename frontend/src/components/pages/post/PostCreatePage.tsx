import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { postApi } from '../../../api/post';
import './PostCreatePage.scss';

const PostCreatePage: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const boardIdFromUrl = searchParams.get('boardId');

  const [formData, setFormData] = useState({
    boardId: boardIdFromUrl ? Number(boardIdFromUrl) : 1, // URL에서 받은 boardId 사용
    title: '',
    content: '',
    isAnonymous: false,
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [images, setImages] = useState<File[]>([]);
  const [imagePreviews, setImagePreviews] = useState<string[]>([]);
  const [uploadingImages, setUploadingImages] = useState(false);

  const boards = [
    { id: 1, name: '中古マーケット' },
    { id: 2, name: '自由掲示板' },
    { id: 3, name: 'イベント掲示板' },
    { id: 4, name: '質問掲示板' },
    { id: 5, name: '人気投稿' },
  ];

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    const checked = (e.target as HTMLInputElement).checked;

    setFormData((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : type === 'select-one' ? Number(value) : value,
    }));
  };

  const handleImageSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;
    if (!files) return;

    const selectedFiles = Array.from(files);

    // 最大5個まで許可
    if (images.length + selectedFiles.length > 5) {
      alert('画像は最大5個までアップロード可能です。');
      return;
    }

    // 各ファイルサイズチェック（10MB）
    const invalidFiles = selectedFiles.filter(file => file.size > 10 * 1024 * 1024);
    if (invalidFiles.length > 0) {
      alert('ファイルサイズは10MB以下である必要があります。');
      return;
    }

    // 画像ファイルのみ許可
    const imageFiles = selectedFiles.filter(file => file.type.startsWith('image/'));
    if (imageFiles.length !== selectedFiles.length) {
      alert('画像ファイルのみアップロード可能です。');
      return;
    }

    // 画像プレビュー生成
    imageFiles.forEach(file => {
      const reader = new FileReader();
      reader.onloadend = () => {
        setImagePreviews(prev => [...prev, reader.result as string]);
      };
      reader.readAsDataURL(file);
    });

    setImages(prev => [...prev, ...imageFiles]);
  };

  const handleImageRemove = (index: number) => {
    setImages(prev => prev.filter((_, i) => i !== index));
    setImagePreviews(prev => prev.filter((_, i) => i !== index));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    // バリデーション
    if (!formData.title.trim()) {
      setError('タイトルを入力してください。');
      return;
    }
    if (!formData.content.trim()) {
      setError('内容を入力してください。');
      return;
    }

    setLoading(true);

    try {
      // 画像アップロード
      const imageUrls: string[] = [];
      if (images.length > 0) {
        setUploadingImages(true);

        for (const image of images) {
          const formData = new FormData();
          formData.append('file', image);

          const apiUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080';
          const response = await fetch(`${apiUrl}/api/images/upload`, {
            method: 'POST',
            credentials: 'include',
            body: formData,
          });

          const result = await response.json();
          if (result.success) {
            imageUrls.push(result.imageUrl);
          } else {
            throw new Error(result.message || '画像アップロード失敗');
          }
        }

        setUploadingImages(false);
      }

      // 投稿作成（画像URL含む）
      const response = await postApi.createPost({
        ...formData,
        imageUrls: imageUrls.length > 0 ? imageUrls : undefined,
      });

      if (response.success) {
        alert(response.message);
        navigate(`/posts/${response.postId}`);
      } else {
        setError(response.message);
      }
    } catch (err: any) {
      setError(err.message || '投稿作成中にエラーが発生しました。');
      console.error('投稿作成エラー:', err);
      setUploadingImages(false);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="post-create-page">
      <div className="post-create-container">
        <h1 className="page-title">投稿作成</h1>

        <form onSubmit={handleSubmit} className="post-create-form">
          {!boardIdFromUrl && (
            <div className="form-group">
              <label htmlFor="boardId">掲示板</label>
              <select
                id="boardId"
                name="boardId"
                value={formData.boardId}
                onChange={handleChange}
                required
              >
                {boards.map((board) => (
                  <option key={board.id} value={board.id}>
                    {board.name}
                  </option>
                ))}
              </select>
            </div>
          )}
          {boardIdFromUrl && (
            <div className="form-group">
              <label>掲示板</label>
              <div className="selected-board">
                {boards.find(b => b.id === formData.boardId)?.name}
              </div>
            </div>
          )}

          <div className="form-group">
            <label htmlFor="title">タイトル</label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleChange}
              placeholder="タイトルを入力してください"
              required
              maxLength={200}
            />
          </div>

          <div className="form-group">
            <label htmlFor="content">内容</label>
            <textarea
              id="content"
              name="content"
              value={formData.content}
              onChange={handleChange}
              placeholder="内容を入力してください"
              required
              rows={15}
            />
          </div>

          <div className="form-group">
            <label htmlFor="images">画像添付（最大5個）</label>
            <input
              type="file"
              id="images"
              accept="image/*"
              multiple
              onChange={handleImageSelect}
              disabled={loading || uploadingImages}
            />
            {imagePreviews.length > 0 && (
              <div className="image-previews">
                {imagePreviews.map((preview, index) => (
                  <div key={index} className="image-preview-item">
                    <img src={preview} alt={`プレビュー ${index + 1}`} />
                    <button
                      type="button"
                      className="remove-image-btn"
                      onClick={() => handleImageRemove(index)}
                      disabled={loading || uploadingImages}
                    >
                      ×
                    </button>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="form-group checkbox-group">
            <label>
              <input
                type="checkbox"
                name="isAnonymous"
                checked={formData.isAnonymous}
                onChange={handleChange}
              />
              匿名で作成
            </label>
          </div>

          {error && <div className="error-message">{error}</div>}
          {uploadingImages && <div className="info-message">画像アップロード中...</div>}

          <div className="button-group">
            <button type="button" className="cancel-button" onClick={() => navigate(-1)} disabled={loading}>
              キャンセル
            </button>
            <button type="submit" className="submit-button" disabled={loading}>
              {loading ? '作成中...' : '作成完了'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default PostCreatePage;
