package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.QnABoardDTO;
import com.roomgenius.furniture_recommendation.entity.QnABoardVO;
import com.roomgenius.furniture_recommendation.entity.UserVO;
import com.roomgenius.furniture_recommendation.mapper.QnABoardMapper;
import com.roomgenius.furniture_recommendation.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class QnABoardServiceImpl implements QnABoardService {

    private final QnABoardMapper qnABoardMapper;
    private final UserMapper userMapper;

    /** ==================== 게시글 등록 ==================== */
    @Override
    @Transactional
    public Integer insert(QnABoardDTO dto) {

        validateBoardDTO(dto);

        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("사용자 ID가 필수입니다.");
        }

        UserVO user = userMapper.findById(dto.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }

        QnABoardVO vo = QnABoardVO.builder()
                .userId(dto.getUserId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        int row = qnABoardMapper.insert(vo);
        if (row == 0) {
            throw new RuntimeException("게시글 등록 실패");
        }

        dto.setQnaBoardId(vo.getQnaBoardId());
        log.info("📌 게시글 등록 완료 | boardId={}", vo.getQnaBoardId());

        return vo.getQnaBoardId();
    }

    /** ==================== 전체 조회 ==================== */
    @Override
    public List<QnABoardVO> selectAll() {
        return qnABoardMapper.selectAll();
    }

    /** ==================== 상세 조회 ==================== */
    @Override
    public QnABoardVO selectById(Integer qnaBoardId) {

        if (qnaBoardId == null) {
            throw new IllegalArgumentException("게시글 ID가 필수입니다.");
        }

        QnABoardVO vo = qnABoardMapper.selectById(qnaBoardId);
        if (vo == null) {
            throw new NoSuchElementException("존재하지 않는 게시글입니다.");
        }

        return vo;
    }

    /** ==================== 게시글 수정 ==================== */
    @Override
    @Transactional
    public Integer update(QnABoardDTO dto, Integer requestUserId) {

        if (requestUserId == null) {
            throw new IllegalArgumentException("요청한 사용자 ID가 필요합니다.");
        }

        validateBoardDTO(dto);

        if (dto.getQnaBoardId() == null) {
            throw new IllegalArgumentException("게시글 ID가 필수입니다.");
        }

        // 기존 게시글 조회
        QnABoardVO existing = selectById(dto.getQnaBoardId());

        // 🔥 본인 글인지 검증
        if (!existing.getUserId().equals(requestUserId)) {
            throw new IllegalStateException("본인 게시글만 수정할 수 있습니다.");
        }

        QnABoardVO vo = QnABoardVO.builder()
                .qnaBoardId(dto.getQnaBoardId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        int result = qnABoardMapper.update(vo);
        if (result == 0) {
            throw new RuntimeException("게시글 수정 실패");
        }

        log.info("✏ 수정 완료 | boardId={}", dto.getQnaBoardId());
        return result;
    }

    /** ==================== 게시글 삭제 ==================== */
    @Override
    @Transactional
    public Integer delete(Integer qnaBoardId, Integer requestUserId) {

        if (requestUserId == null) {
            throw new IllegalArgumentException("요청한 사용자 ID가 필요합니다.");
        }

        if (qnaBoardId == null) {
            throw new IllegalArgumentException("게시글 ID가 필수입니다.");
        }

        QnABoardVO existing = selectById(qnaBoardId);

        // 🔥 본인 글인지 검증
        if (!existing.getUserId().equals(requestUserId)) {
            throw new IllegalStateException("본인 게시글만 삭제할 수 있습니다.");
        }

        int result = qnABoardMapper.delete(qnaBoardId);
        if (result == 0) {
            throw new RuntimeException("게시글 삭제 실패");
        }

        log.info("🗑 게시글 삭제 완료 | boardId={}", qnaBoardId);
        return result;
    }

    /** ==================== DTO 검증 ==================== */
    private void validateBoardDTO(QnABoardDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("게시글 데이터가 없습니다.");
        }
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용은 필수입니다.");
        }
    }
}
