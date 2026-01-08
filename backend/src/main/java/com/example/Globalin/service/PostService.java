package com.example.Globalin.service;

import com.example.Globalin.dao.PostDao;
import com.example.Globalin.dao.PostImageDao;
import com.example.Globalin.dao.PostLikeDao;
import com.example.Globalin.dao.UserDao;
import com.example.Globalin.dto.request.CreatePostRequest;
import com.example.Globalin.dto.request.UpdatePostRequest;
import com.example.Globalin.dto.response.PostResponse;
import com.example.Globalin.model.Post;
import com.example.Globalin.model.PostImage;
import com.example.Globalin.model.PostLike;
import com.example.Globalin.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private PostImageDao postImageDao;

    @Autowired
    private PostLikeDao postLikeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public PostResponse createPost(CreatePostRequest request, Long userId) {
        try {
            // バリデーション
            if (request.getBoardId() == null) {
                return new PostResponse(false, "掲示板を選択してください");
            }
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return new PostResponse(false, "タイトルを入力してください");
            }
            if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                return new PostResponse(false, "内容を入力してください");
            }

            // 投稿作成
            Post post = new Post();
            post.setBoardId(request.getBoardId());
            post.setAuthorId(userId);
            post.setTitle(request.getTitle());
            post.setContent(request.getContent());
            post.setIsAnonymous(request.getIsAnonymous() != null ? request.getIsAnonymous() : false);

            postDao.insertPost(post);

            // 画像保存
            if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
                for (int i = 0; i < request.getImageUrls().size(); i++) {
                    String imageUrl = request.getImageUrls().get(i);
                    PostImage postImage = new PostImage();
                    postImage.setPostId(post.getId());
                    postImage.setImageUrl(imageUrl);
                    postImage.setOriginalFilename(imageUrl.substring(imageUrl.lastIndexOf("/") + 1));
                    postImage.setDisplayOrder(i);
                    postImageDao.insertImage(postImage);
                }

                // 投稿のimage_count更新
                postDao.updateImageCount(post.getId(), request.getImageUrls().size());
            }

            return new PostResponse(true, "投稿が作成されました", post.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return new PostResponse(false, "投稿作成中にエラーが発生しました: " + e.getMessage());
        }
    }

    public Post getPostById(Long id) {
        Post post = postDao.findById(id);
        if (post != null) {
            // 閲覧数増加
            postDao.incrementViewCount(id);
            // 画像リスト取得
            List<PostImage> images = postImageDao.findImagesByPostId(id);
            post.setImages(images);
        }
        return post;
    }

    public List<Post> getPostsByBoardId(Long boardId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return postDao.findByBoardId(boardId, offset, pageSize);
    }

    public List<Post> getAllPosts(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return postDao.findAll(offset, pageSize);
    }

    public List<Post> getPostsByAuthorId(Long authorId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return postDao.findByAuthorId(authorId, offset, pageSize);
    }

    @Transactional
    public PostResponse updatePost(Long postId, UpdatePostRequest request, Long userId) {
        try {
            // 投稿存在確認
            Post post = postDao.findById(postId);
            if (post == null) {
                return new PostResponse(false, "投稿が見つかりません");
            }

            // 作成者確認
            if (!post.getAuthorId().equals(userId)) {
                return new PostResponse(false, "投稿を編集する権限がありません");
            }

            // バリデーション
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return new PostResponse(false, "タイトルを入力してください");
            }
            if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                return new PostResponse(false, "内容を入力してください");
            }

            // 投稿更新
            post.setTitle(request.getTitle());
            post.setContent(request.getContent());
            postDao.updatePost(post);

            return new PostResponse(true, "投稿が更新されました", postId);

        } catch (Exception e) {
            e.printStackTrace();
            return new PostResponse(false, "投稿更新中にエラーが発生しました: " + e.getMessage());
        }
    }

    @Transactional
    public PostResponse deletePost(Long postId, Long userId) {
        try {
            // 投稿存在確認
            Post post = postDao.findById(postId);
            if (post == null) {
                return new PostResponse(false, "投稿が見つかりません");
            }

            // 作成者確認
            if (!post.getAuthorId().equals(userId)) {
                return new PostResponse(false, "投稿を削除する権限がありません");
            }

            // 投稿削除（ソフト削除）
            postDao.deletePost(postId);

            return new PostResponse(true, "投稿が削除されました");

        } catch (Exception e) {
            e.printStackTrace();
            return new PostResponse(false, "投稿削除中にエラーが発生しました: " + e.getMessage());
        }
    }

    public List<Post> searchPosts(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return postDao.searchPosts(keyword, offset, pageSize);
    }

    /**
     * 投稿「いいね」トグル（追加/キャンセル）
     */
    @Transactional
    public PostResponse toggleLike(Long postId, Long userId) {
        try {
            // 投稿存在確認
            Post post = postDao.findById(postId);
            if (post == null) {
                return new PostResponse(false, "投稿が見つかりません");
            }

            // 既に「いいね」を押したかどうか確認
            int likeCount = postLikeDao.existsByPostIdAndUserId(postId, userId);

            if (likeCount > 0) {
                // 「いいね」キャンセル
                postLikeDao.deleteLike(postId, userId);
                return new PostResponse(true, "いいねがキャンセルされました");
            } else {
                // 「いいね」追加
                PostLike postLike = new PostLike();
                postLike.setPostId(postId);
                postLike.setUserId(userId);
                postLikeDao.insertLike(postLike);

                // 通知作成（投稿作成者へ）
                User liker = userDao.findById(userId);
                String likerName = liker.getNickname() != null ? liker.getNickname() : liker.getUsername();
                String message = String.format("%sさんがあなたの投稿「%s」をいいねしました",
                    likerName,
                    post.getTitle().length() > 20 ? post.getTitle().substring(0, 20) + "..." : post.getTitle());

                notificationService.createNotification(
                    post.getAuthorId(),  // 投稿作成者
                    "LIKE",
                    postId,
                    userId,
                    likerName,
                    message
                );

                return new PostResponse(true, "いいねを押しました");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new PostResponse(false, "いいね処理中にエラーが発生しました: " + e.getMessage());
        }
    }

    /**
     * 投稿のいいね数取得
     */
    public int getLikeCount(Long postId) {
        return postLikeDao.countByPostId(postId);
    }

    /**
     * ユーザーが投稿に「いいね」を押したかどうか確認
     */
    public boolean isLikedByUser(Long postId, Long userId) {
        return postLikeDao.existsByPostIdAndUserId(postId, userId) > 0;
    }
}
