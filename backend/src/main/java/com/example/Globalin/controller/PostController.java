package com.example.Globalin.controller;

import com.example.Globalin.dto.request.CreatePostRequest;
import com.example.Globalin.dto.request.UpdatePostRequest;
import com.example.Globalin.dto.response.PostResponse;
import com.example.Globalin.model.Post;
import com.example.Globalin.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PostController {

    @Autowired
    private PostService postService;

    // 投稿作成
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody CreatePostRequest request,
                                                    HttpSession session) {
        // セッションからユーザーIDを取得 (ログイン確認)
        System.out.println("Session ID: " + session.getId());
        System.out.println("Session userId: " + session.getAttribute("userId"));

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            System.out.println("userId is null - ログインが必要");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new PostResponse(false, "ログインが必要です。"));
        }

        PostResponse response = postService.createPost(request, userId);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 投稿詳細照会
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        Post post = postService.getPostById(id);

        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 掲示板別投稿リスト
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<Post>> getPostsByBoard(@PathVariable Long boardId,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "20") int pageSize) {
        List<Post> posts = postService.getPostsByBoardId(boardId, page, pageSize);
        return ResponseEntity.ok(posts);
    }

    // 全投稿リスト
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "20") int pageSize) {
        List<Post> posts = postService.getAllPosts(page, pageSize);
        return ResponseEntity.ok(posts);
    }

    // 自分が作成した投稿リスト
    @GetMapping("/my")
    public ResponseEntity<?> getMyPosts(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "20") int pageSize,
                                        HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new PostResponse(false, "ログインが必要です。"));
        }

        List<Post> posts = postService.getPostsByAuthorId(userId, page, pageSize);
        return ResponseEntity.ok(posts);
    }

    // 投稿修正
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id,
                                                    @RequestBody UpdatePostRequest request,
                                                    HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new PostResponse(false, "ログインが必要です。"));
        }

        PostResponse response = postService.updatePost(id, request, userId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 投稿削除
    @DeleteMapping("/{id}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable Long id,
                                                    HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new PostResponse(false, "ログインが必要です。"));
        }

        PostResponse response = postService.deletePost(id, userId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 投稿検索
    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam String keyword,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "20") int pageSize) {
        List<Post> posts = postService.searchPosts(keyword, page, pageSize);
        return ResponseEntity.ok(posts);
    }

    // いいねトグル (追加/取消)
    @PostMapping("/{id}/like")
    public ResponseEntity<PostResponse> toggleLike(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new PostResponse(false, "ログインが必要です。"));
        }

        PostResponse response = postService.toggleLike(id, userId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // いいね数照会
    @GetMapping("/{id}/like/count")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long id) {
        int count = postService.getLikeCount(id);
        return ResponseEntity.ok(count);
    }

    // いいね状態照会 (現在のユーザーがいいねを押したか)
    @GetMapping("/{id}/like/status")
    public ResponseEntity<Boolean> getLikeStatus(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(false);
        }

        boolean isLiked = postService.isLikedByUser(id, userId);
        return ResponseEntity.ok(isLiked);
    }
}
