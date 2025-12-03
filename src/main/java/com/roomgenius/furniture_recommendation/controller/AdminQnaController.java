package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.QnABoardVO;
import com.roomgenius.furniture_recommendation.service.AdminQnABoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/qna")
public class AdminQnaController {

    private final AdminQnABoardService adminQnABoardService;

    /** 전체 Q&A 목록 가져오기 */
    @GetMapping
    public List<QnABoardVO> list() {
        return adminQnABoardService.selectAll();
    }

    /** 단일 조회 */
    @GetMapping("/{id}")
    public QnABoardVO getOne(@PathVariable Integer id) {
        return adminQnABoardService.selectById(id);
    }

    /** 답변 등록/수정 */
    @PutMapping("/{id}/answer")
    public ResponseEntity<String> answer(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body
    ) {
        adminQnABoardService.updateAnswer(id, body.get("answer"));
        return ResponseEntity.ok("answered");
    }

    /** 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        adminQnABoardService.delete(id);
        return ResponseEntity.ok("deleted");
    }
}





