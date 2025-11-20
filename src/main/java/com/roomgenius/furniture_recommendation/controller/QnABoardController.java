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
import org.springframework.http.HttpStatus;
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

    /* ================================
     * 공통 응답 유틸
     * ================================ */
    private ResponseEntity<Map<String, Object>> ok(Map<String, Object> body) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.putAll(body);
        return ResponseEntity.ok(res);
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", false);
        res.put("message", message);
        return ResponseEntity.status(status).body(res);
    }

    /* ================================
     * 1. 게시글 등록
     * ================================ */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> insertBoard(
            @Valid @RequestPart("board") QnABoardDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader(value = "Authorization", required = false) String tokenHeader) {

        try {
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                return error(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
            }

            String token = tokenHeader.substring(7);
            String email = jwtTokenProvider.getEmailFromToken(token);

            UserVO user = userService.findByEmail(email);
            if (user == null) {
                return error(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자입니다.");
            }

            dto.setUserId(user.getUserId());

            Integer boardId = qnABoardService.insert(dto);

            if (images != null && !images.isEmpty()) {
                fileService.uploadQnaFiles(boardId, images);
            }

            return ok(Map.of(
                    "message", "게시글 등록 성공",
                    "qnaBoardId", boardId,
                    "fileCount", images != null ? images.size() : 0
            ));

        } catch (IllegalArgumentException e) {
            return error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException e) {
            return error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            return error(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            log.error("게시글 등록 중 오류", e);
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 등록 중 서버 오류가 발생했습니다.");
        }
    }

    /* ================================
     * 2. 전체 조회
     * ================================ */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBoards() {
        try {
            List<QnABoardVO> list = qnABoardService.selectAll();

            return ok(Map.of(
                    "count", list.size(),
                    "data", list
            ));
        } catch (Exception e) {
            log.error("게시글 전체 조회 중 오류", e);
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 조회 중 서버 오류가 발생했습니다.");
        }
    }

    /* ================================
     * 3. 상세 조회
     * ================================ */
    @GetMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> getBoardById(@PathVariable int boardId) {
        try {
            QnABoardVO board = qnABoardService.selectById(boardId); // Service에서 예외 throw
            List<FileVO> files = fileService.getQnaFiles(boardId);

            Map<String, Object> data = new HashMap<>();
            data.put("board", board);
            data.put("files", files);

            return ok(Map.of("data", data));

        } catch (IllegalArgumentException e) {
            return error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException e) {
            return error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("게시글 상세 조회 중 오류", e);
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 조회 중 서버 오류가 발생했습니다.");
        }
    }

    /* ================================
     * 4. 수정
     * ================================ */
    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> updateBoard(
            @PathVariable int boardId,
            @Valid @RequestPart("board") QnABoardDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader(value = "Authorization", required = false) String tokenHeader) {

        try {
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                return error(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
            }

            String token = tokenHeader.substring(7);
            String email = jwtTokenProvider.getEmailFromToken(token);

            UserVO user = userService.findByEmail(email);
            if (user == null) {
                return error(HttpStatus.UNAUTHORIZED, "로그인 정보가 유효하지 않습니다.");
            }

            dto.setQnaBoardId(boardId);

            // 🔥 본인 여부 / 존재 여부 / 유효성 검증은 Service에서 처리
            qnABoardService.update(dto, user.getUserId());

            boolean replaced = false;

            if (images != null && !images.isEmpty()) {
                replaced = true;
                fileService.deleteQnaFiles(boardId);
                fileService.uploadQnaFiles(boardId, images);
            }

            return ok(Map.of(
                    "message", "게시글 수정 성공",
                    "imagesReplaced", replaced
            ));

        } catch (IllegalArgumentException e) {
            return error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException e) {
            return error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            // 💡 ServiceImpl.update에서 본인 아니면 IllegalStateException
            return error(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            log.error("게시글 수정 중 오류", e);
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 수정 중 서버 오류가 발생했습니다.");
        }
    }

    /* ================================
     * 5. 삭제
     * ================================ */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> deleteBoard(
            @PathVariable int boardId,
            @RequestHeader(value = "Authorization", required = false) String tokenHeader) {

        try {
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                return error(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
            }

            String token = tokenHeader.substring(7);
            String email = jwtTokenProvider.getEmailFromToken(token);

            UserVO user = userService.findByEmail(email);
            if (user == null) {
                return error(HttpStatus.UNAUTHORIZED, "로그인 정보가 유효하지 않습니다.");
            }

            // 🔥 존재 여부 + 본인 여부 검증은 Service 쪽에서
            qnABoardService.delete(boardId, user.getUserId());

            // 물리 파일 + 파일 테이블 정리
            fileService.deleteQnaFiles(boardId);

            return ok(Map.of(
                    "message", "게시글 삭제 성공"
            ));

        } catch (IllegalArgumentException e) {
            return error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException e) {
            return error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            return error(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            log.error("게시글 삭제 중 오류", e);
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 삭제 중 서버 오류가 발생했습니다.");
        }
    }
}
