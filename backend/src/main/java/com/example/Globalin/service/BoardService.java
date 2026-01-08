package com.example.Globalin.service;

import com.example.Globalin.model.Post;
import com.example.Globalin.model.HotPost;
import com.example.Globalin.model.Board;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BoardService {

    public List<Post> getRecentPosts(String boardType, int limit) {
        // TODO: 実際のデータベース連携時に実装
        // 仮のモックデータを返却
        List<Post> posts = new ArrayList<>();

        if ("humanities".equals(boardType)) {
            Post post1 = new Post();
            post1.setId(1L);
            post1.setTitle("日本の大学生活適応のコツをシェアします");
            post1.setContent("こんにちは。日本の大学に来て1年経ちましたので、感じたことをシェアしようと思います...");
            post1.setAuthor("留学太郎");
            post1.setAuthorId(2L);
            post1.setCreatedAt(new Date());
            post1.setViewCount(156);
            post1.setCommentCount(23);
            post1.setLikeCount(45);
            post1.setBoardId(1L);
            post1.setBoardName("人文学");
            posts.add(post1);

            Post post2 = new Post();
            post2.setId(2L);
            post2.setTitle("日本語学習方法のお勧め");
            post2.setContent("効果的な日本語学習法についてお話しましょう");
            post2.setAuthor("学生B");
            post2.setAuthorId(3L);
            post2.setCreatedAt(new Date());
            post2.setViewCount(89);
            post2.setCommentCount(12);
            post2.setLikeCount(28);
            post2.setBoardId(1L);
            post2.setBoardName("人文学");
            posts.add(post2);
        } else if ("free".equals(boardType)) {
            Post post1 = new Post();
            post1.setId(3L);
            post1.setTitle("東京の美味しい飲食店をお勧めしてください");
            post1.setContent("今週末に東京旅行に行くので、美味しい飲食店のお勧めをお願いします！");
            post1.setAuthor("旅行好きさん");
            post1.setAuthorId(4L);
            post1.setCreatedAt(new Date());
            post1.setViewCount(234);
            post1.setCommentCount(31);
            post1.setLikeCount(18);
            post1.setBoardId(2L);
            post1.setBoardName("自由掲示板");
            posts.add(post1);

            Post post2 = new Post();
            post2.setId(4L);
            post2.setTitle("寮 vs ワンルーム どちらが良いでしょうか？");
            post2.setContent("来年から一人暮らしを検討中ですが、アドバイスをお願いします。");
            post2.setAuthor("迷い中さん");
            post2.setAuthorId(5L);
            post2.setCreatedAt(new Date());
            post2.setViewCount(178);
            post2.setCommentCount(26);
            post2.setLikeCount(15);
            post2.setBoardId(2L);
            post2.setBoardName("自由掲示板");
            posts.add(post2);
        }

        return posts;
    }

    public List<HotPost> getHotPosts(int limit) {
        // TODO: 実際のデータベース連携時に実装
        List<HotPost> hotPosts = new ArrayList<>();

        HotPost hot1 = new HotPost();
        hot1.setId(5L);
        hot1.setTitle("日本の大学奨学金情報まとめ");
        hot1.setAuthor("情報マスター");
        hot1.setViewCount(892);
        hot1.setCommentCount(67);
        hot1.setLikeCount(134);
        hot1.setCreatedAt(new Date());
        hot1.setIsHot(true);
        hotPosts.add(hot1);

        HotPost hot2 = new HotPost();
        hot2.setId(6L);
        hot2.setTitle("留学生のアルバイトお勧め");
        hot2.setAuthor("アルバイト達人");
        hot2.setViewCount(645);
        hot2.setCommentCount(45);
        hot2.setLikeCount(89);
        hot2.setCreatedAt(new Date());
        hot2.setIsHot(true);
        hotPosts.add(hot2);

        HotPost hot3 = new HotPost();
        hot3.setId(7L);
        hot3.setTitle("ビザ延長の体験記");
        hot3.setAuthor("ビザマスター");
        hot3.setViewCount(521);
        hot3.setCommentCount(38);
        hot3.setLikeCount(72);
        hot3.setCreatedAt(new Date());
        hot3.setIsHot(true);
        hotPosts.add(hot3);

        return hotPosts;
    }

    public List<Board> getBestBoards(int limit) {
        // TODO: 実際のデータベース連携時に実装
        List<Board> boards = new ArrayList<>();

        Board board1 = new Board();
        board1.setId(1L);
        board1.setName("人文学");
        board1.setDescription("文学、哲学、歴史など");
        board1.setPostCount(234);
        board1.setCategory("学術");
        board1.setIcon("📚");
        boards.add(board1);

        Board board2 = new Board();
        board2.setId(2L);
        board2.setName("自由掲示板");
        board2.setDescription("自由なテーマの掲示板");
        board2.setPostCount(567);
        board2.setCategory("一般");
        board2.setIcon("💬");
        boards.add(board2);

        Board board3 = new Board();
        board3.setId(3L);
        board3.setName("就職/進路");
        board3.setDescription("就職および進路情報");
        board3.setPostCount(189);
        board3.setCategory("進路");
        board3.setIcon("💼");
        boards.add(board3);

        return boards;
    }
}
