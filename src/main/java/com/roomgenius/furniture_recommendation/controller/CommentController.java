package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.config.JwtTokenProvider;
import com.roomgenius.furniture_recommendation.entity.CommentDTO;
import com.roomgenius.furniture_recommendation.entity.CommentVO;
import com.roomgenius.furniture_recommendation.entity.UserVO;
import com.roomgenius.furniture_recommendation.mapper.UserMapper;
import com.roomgenius.furniture_recommendation.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    /**
     * 댓글 작성
     */
    @PostMapping
    public ResponseEntity<?> createComment(
            @RequestHeader("Authorization") String token,
            @RequestBody CommentDTO commentDTO) {

        String email = jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
        UserVO user = userMapper.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(401).body("사용자를 찾을 수 없습니다.");
        }

        commentDTO.setUserId(user.getUserId());
        commentService.insertComment(commentDTO);

        return ResponseEntity.ok("댓글이 등록되었습니다.");
    }

    /**
     * 게시글별 댓글 목록 조회
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<CommentVO>> getCommentsByBoardId(@PathVariable Integer boardId) {
        List<CommentVO> comments = commentService.getComments(boardId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 단건 조회
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentVO> getComment(@PathVariable Integer commentId) {
        CommentVO comment = commentService.getComment(commentId);
        return ResponseEntity.ok(comment);
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer commentId,
            @RequestBody CommentDTO commentDTO) {

        String email = jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
        UserVO user = userMapper.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(401).body("사용자를 찾을 수 없습니다.");
        }

        commentDTO.setCommentId(commentId);
        commentDTO.setUserId(user.getUserId());
        commentService.updateComment(commentDTO);

        return ResponseEntity.ok("댓글이 수정되었습니다.");
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer commentId) {

        String email = jwtTokenProvider.getEmailFromToken(token.replace("Bearer ", ""));
        UserVO user = userMapper.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(401).body("사용자를 찾을 수 없습니다.");
        }

        commentService.deleteComment(commentId, user.getUserId());

        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}