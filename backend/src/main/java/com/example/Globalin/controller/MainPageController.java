package com.example.Globalin.controller;

import com.example.Globalin.dao.BoardDao;
import com.example.Globalin.dto.MainPageDTO;
import com.example.Globalin.model.Board;
import com.example.Globalin.model.Post;
import com.example.Globalin.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/main")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MainPageController {

    @Autowired
    private PostService postService;

    @Autowired
    private BoardDao boardDao;

    @GetMapping("/dashboard")
    public ResponseEntity<MainPageDTO> getDashboard(HttpSession session) {
        MainPageDTO dashboard = new MainPageDTO();

        // 사용자 프로필 (로그인 여부 확인)
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            MainPageDTO.UserProfile userProfile = new MainPageDTO.UserProfile();
            userProfile.setNickname((String) session.getAttribute("nickname"));
            userProfile.setPostCount(0); // TODO: 실제 게시글 수 조회
            userProfile.setCommentCount(0); // TODO: 실제 댓글 수 조회
            dashboard.setUserProfile(userProfile);
        }

        // 인문학 게시판 최신 게시글 (게시판 ID 1)
        List<Post> humanitiesPosts = postService.getPostsByBoardId(1L, 1, 5);
        dashboard.setHumanitiesPosts(humanitiesPosts);

        // 자유게시판 최신 게시글 (게시판 ID 2)
        List<Post> freePosts = postService.getPostsByBoardId(2L, 1, 5);
        dashboard.setFreePosts(freePosts);

        // Hot 게시글 (조회수 높은 게시글)
        List<Post> allPosts = postService.getAllPosts(1, 10);
        List<MainPageDTO.HotPost> hotPosts = new ArrayList<>();
        for (int i = 0; i < Math.min(5, allPosts.size()); i++) {
            Post post = allPosts.get(i);
            MainPageDTO.HotPost hotPost = new MainPageDTO.HotPost();
            hotPost.setId(post.getId());
            hotPost.setTitle(post.getTitle());
            hotPost.setViewCount(post.getViewCount());
            hotPost.setLikeCount(post.getLikeCount());
            hotPost.setCommentCount(post.getCommentCount());
            hotPosts.add(hotPost);
        }
        dashboard.setHotPosts(hotPosts);

        // Best 게시판 (하드코딩된 게시판 목록)
        List<MainPageDTO.Board> bestBoards = new ArrayList<>();
        String[] boardNames = {"인문학", "자유게시판", "질문게시판", "스터디", "밥/파티"};
        for (int i = 0; i < boardNames.length; i++) {
            MainPageDTO.Board board = new MainPageDTO.Board();
            board.setId((long) (i + 1));
            board.setName(boardNames[i]);
            board.setPostCount(0); // TODO: 실제 게시글 수 조회
            bestBoards.add(board);
        }
        dashboard.setBestBoards(bestBoards);

        // 모든 게시판과 각 게시판의 최신 게시글 5개
        List<Board> allBoardsList = boardDao.findAllBoards();
        List<MainPageDTO.BoardWithPosts> allBoards = new ArrayList<>();
        for (Board board : allBoardsList) {
            MainPageDTO.BoardWithPosts boardWithPosts = new MainPageDTO.BoardWithPosts();
            boardWithPosts.setId(board.getId());
            boardWithPosts.setName(board.getName());
            boardWithPosts.setDescription(board.getDescription());
            boardWithPosts.setCategory(board.getCategory());
            boardWithPosts.setIcon(board.getIcon());
            boardWithPosts.setPostCount(board.getPostCount());

            // 각 게시판의 최신 게시글 5개 조회
            List<Post> recentPosts = postService.getPostsByBoardId(board.getId(), 1, 5);
            boardWithPosts.setRecentPosts(recentPosts);

            allBoards.add(boardWithPosts);
        }
        dashboard.setAllBoards(allBoards);

        return ResponseEntity.ok(dashboard);
    }
}