package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.config.JwtTokenProvider;
import com.roomgenius.furniture_recommendation.entity.*;
import com.roomgenius.furniture_recommendation.service.FileService;
import com.roomgenius.furniture_recommendation.service.QnABoardService;
import com.roomgenius.furniture_recommendation.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/qnaboards")
public class QnABoardController {

    private final QnABoardService qnABoardService;
    private final FileService fileService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    /** ==================== 게시글 등록 ==================== */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertBoard(
            @Valid @RequestPart("board") QnABoardDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader("Authorization") String tokenHeader) {

        // JWT → email
        String email = jwtTokenProvider.getEmailFromToken(tokenHeader.substring(7));

        UserVO user = userService.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        dto.setUserId(user.getUserId());

        // 게시글 등록 → boardId 반환
        Integer boardId = qnABoardService.insert(dto);

        // 이미지 저장
        if (images != null && !images.isEmpty()) {
            fileService.uploadQnaFiles(boardId, images);
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "게시글 등록 성공",
                "qnaBoardId", boardId,
                "fileCount", images != null ? images.size() : 0
        ));
    }

    /** ==================== 전체 조회 ==================== */
    @GetMapping
    public ResponseEntity<?> getAllBoards() {

        List<QnABoardVO> list = qnABoardService.selectAll();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "count", list.size(),
                "data", list
        ));
    }

    /** ==================== 상세 조회 ==================== */
    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoardById(@PathVariable int boardId) {

        QnABoardVO board = qnABoardService.selectById(boardId);

        List<FileVO> files = fileService.getQnaFiles(boardId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                        "board", board,
                        "files", files
                )
        ));
    }

    /** ==================== QnA 게시글 수정 ==================== */
    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBoard(
            @PathVariable int boardId,
            @Valid @RequestPart("board") QnABoardDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader("Authorization") String tokenHeader) {

        String email = jwtTokenProvider.getEmailFromToken(tokenHeader.substring(7));
        UserVO user = userService.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        dto.setQnaBoardId(boardId);

        // 1) 게시글 수정
        qnABoardService.update(dto, user.getUserId());

        // 2) 이미지 교체 처리
        boolean replaced = false;

        if (images != null && !images.isEmpty()) {
            replaced = true;

            // ⭐⭐ QnA 용으로 교체해야 함 (문제의 핵심 부분)
            fileService.deleteQnaFiles(boardId);
            fileService.uploadQnaFiles(boardId, images);
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "QnA 게시글 수정 성공",
                "imagesReplaced", replaced
        ));
    }

    /** ==================== 게시글 삭제 ==================== */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(
            @PathVariable int boardId,
            @RequestHeader("Authorization") String tokenHeader) {

        String email = jwtTokenProvider.getEmailFromToken(tokenHeader.substring(7));

        UserVO user = userService.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        // 🔥 본인 검증 + 삭제 → Service에서 처리
        qnABoardService.delete(boardId, user.getUserId());

        // 파일 삭제
        fileService.deleteQnaFiles(boardId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "게시글 삭제 성공"
        ));
    }
}
