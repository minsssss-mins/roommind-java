package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.config.JwtTokenProvider;
import com.roomgenius.furniture_recommendation.entity.FileVO;
import com.roomgenius.furniture_recommendation.entity.QnABoardDTO;
import com.roomgenius.furniture_recommendation.entity.QnABoardVO;
import com.roomgenius.furniture_recommendation.entity.UserVO;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/qnaboards")
public class QnABoardController {

    private final QnABoardService qnABoardService;
    private final FileService fileService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    /** **********************************************
     *  ✅ QnA 게시글 등록 (+ 이미지 첨부 가능)
     *  board(JSON), images(List<MultipartFile>)
     ************************************************ */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> insertBoard(
            @Valid @RequestPart("board") QnABoardDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader("Authorization") String tokenHeader) {

        Map<String, Object> response = new HashMap<>();

        // 1️⃣ JWT 토큰에서 이메일 추출
        String token = tokenHeader.substring(7);
        String email = jwtTokenProvider.getEmailFromToken(token);

        // 2️⃣ email → userId 매핑
        UserVO user = userService.findByEmail(email);
        if (user == null) {
            response.put("success", false);
            response.put("message", "유효하지 않은 사용자입니다.");
            return ResponseEntity.badRequest().body(response);
        }
        dto.setUserId(user.getUserId()); // DTO에 userId 설정

        // 3️⃣ 게시글 텍스트 저장
        Integer result = qnABoardService.insert(dto);

        if (result <= 0) {
            response.put("success", false);
            response.put("message", "게시글 등록 실패");
            return ResponseEntity.badRequest().body(response);
        }

        Integer newBoardId = dto.getQnaBoardId();  // PK 반환됨
        log.info("🆔 생성된 게시글 ID: {}", newBoardId);

        // 4️⃣ 이미지 업로드
        if (images != null && !images.isEmpty()) {
            fileService.uploadQnaFiles(newBoardId, images);
        }

        response.put("success", true);
        response.put("message", "게시글 등록 성공");
        response.put("qnboardId", newBoardId);
        response.put("fileCount", images != null ? images.size() : 0);

        return ResponseEntity.ok(response);
    }

    /** **********************************************
     *  ✅ 게시글 전체 조회 (이미지 제외)
     ************************************************ */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBoards() {
        Map<String, Object> response = new HashMap<>();

        List<QnABoardVO> list = qnABoardService.selectAll();

        response.put("success", true);
        response.put("count", list.size());
        response.put("data", list);

        return ResponseEntity.ok(response);
    }

    /** **********************************************
     *  ✅ 게시글 상세 조회 (게시글 + 이미지 목록)
     ************************************************ */
    @GetMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> getBoardById(@PathVariable int boardId) {

        Map<String, Object> response = new HashMap<>();
        QnABoardVO board = qnABoardService.selectById(boardId);

        if (board == null) {
            response.put("success", false);
            response.put("message", "존재하지 않는 게시글입니다.");
            return ResponseEntity.badRequest().body(response);
        }

        List<FileVO> files = fileService.getQnaFiles(boardId);

        Map<String, Object> data = new HashMap<>();
        data.put("board", board);
        data.put("files", files);

        response.put("success", true);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    /** **********************************************
     *  ✅ 게시글 수정 (+ 이미지 선택적 교체)
     ************************************************ */
    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> updateBoard(
            @PathVariable int boardId,
            @Valid @RequestPart("board") QnABoardDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader("Authorization") String tokenHeader) {

        Map<String, Object> response = new HashMap<>();

        // 1️⃣ JWT 이메일 추출
        String token = tokenHeader.substring(7);
        String email = jwtTokenProvider.getEmailFromToken(token);

        // 2️⃣ 기존 게시글 조회
        QnABoardVO existing = qnABoardService.selectById(boardId);
        if (existing == null) {
            response.put("success", false);
            response.put("message", "존재하지 않는 게시글입니다.");
            return ResponseEntity.badRequest().body(response);
        }

        // 3️⃣ 작성자 검증
        if (!email.equals(existing.getEmail())) {
            response.put("success", false);
            response.put("message", "본인 게시글만 수정 가능합니다.");
            return ResponseEntity.status(403).body(response);
        }

        // 4️⃣ 텍스트 수정
        dto.setQnaBoardId(boardId);
        int result = qnABoardService.update(dto);

        if (result <= 0) {
            response.put("success", false);
            response.put("message", "게시글 수정 실패");
            return ResponseEntity.badRequest().body(response);
        }

        // 5️⃣ 이미지 교체 정책
        boolean replaced = false;

        if (images != null && !images.isEmpty()) {
            replaced = true;

            // 기존 이미지 모두 삭제 (물리 파일 + DB)
            fileService.deleteQnaFiles(boardId);

            // 새 이미지 저장
            fileService.uploadQnaFiles(boardId, images);
        }

        response.put("success", true);
        response.put("message", "게시글 수정 성공");
        response.put("imagesReplaced", replaced);

        return ResponseEntity.ok(response);
    }

    /** **********************************************
     *  ✅ 게시글 삭제 (이미지 포함 전체 삭제)
     ************************************************ */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> deleteBoard(
            @PathVariable int boardId,
            @RequestHeader("Authorization") String tokenHeader) {

        Map<String, Object> response = new HashMap<>();

        String token = tokenHeader.substring(7);
        String email = jwtTokenProvider.getEmailFromToken(token);

        QnABoardVO existing = qnABoardService.selectById(boardId);
        if (existing == null) {
            response.put("success", false);
            response.put("message", "존재하지 않는 게시글입니다.");
            return ResponseEntity.badRequest().body(response);
        }

        // 작성자 검증
        if (!email.equals(existing.getEmail())) {
            response.put("success", false);
            response.put("message", "본인 게시글만 삭제할 수 있습니다.");
            return ResponseEntity.status(403).body(response);
        }

        // 1️⃣ 물리 파일 + DB 삭제
        fileService.deleteQnaFiles(boardId);

        // 2️⃣ 게시글 삭제
        int result = qnABoardService.delete(boardId);

        response.put("success", result > 0);
        response.put("message", "게시글 삭제 성공");

        return ResponseEntity.ok(response);
    }
}
