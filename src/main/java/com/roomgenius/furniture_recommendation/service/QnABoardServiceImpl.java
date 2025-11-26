package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.FileVO;
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

    private final FileService fileService;
    private final QnABoardMapper qnABoardMapper;
    private final UserMapper userMapper;

    /** ==================== 게시글 등록 ==================== */
    @Override
    @Transactional
    public Integer insert(QnABoardDTO dto) {
        try {
            log.info("📌 게시글 등록 요청: {}", dto);
            validateBoardDTO(dto);

            if (dto.getUserId() == null)
                throw new IllegalArgumentException("사용자 ID가 필수입니다.");

            UserVO user = userMapper.findById(dto.getUserId());
            if (user == null)
                throw new IllegalArgumentException("유효하지 않은 사용자입니다.");

            QnABoardVO vo = QnABoardVO.builder()
                    .userId(dto.getUserId())
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .build();

            int row = qnABoardMapper.insert(vo);
            if (row == 0)
                throw new RuntimeException("게시글 등록 실패");

            dto.setQnaBoardId(vo.getQnaBoardId());
            return vo.getQnaBoardId();

        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            log.warn("❌ 잘못된 요청: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ 게시글 등록 중 오류: {}", e.getMessage(), e);
            throw new RuntimeException("게시글 등록 중 서버 오류 발생");
        }
    }

    /** ==================== 전체 조회 ==================== */
    @Override
    public List<QnABoardVO> selectAll() {

        // 1) 전체 게시글 가져오기
        List<QnABoardVO> list = qnABoardMapper.selectAll();

        // 2) 이미지 붙이기
        for (QnABoardVO board : list) {
            List<FileVO> images = fileService.getQnaFiles(board.getQnaBoardId());
            board.setImages(images);  // ★ VO에 images 필드 미리 추가해야 함
        }

        return list;
    }


    /** ==================== 상세 조회 ==================== */
    @Override
    public QnABoardVO selectById(Integer qnaBoardId) {
        try {
            if (qnaBoardId == null)
                throw new IllegalArgumentException("게시글 ID가 필수입니다.");

            QnABoardVO vo = qnABoardMapper.selectById(qnaBoardId);
            if (vo == null)
                throw new NoSuchElementException("존재하지 않는 게시글입니다.");

            return vo;

        } catch (IllegalArgumentException | NoSuchElementException e) {
            log.warn("❌ 조회 오류: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ 상세 조회 중 오류", e);
            throw new RuntimeException("상세 조회 중 서버 오류 발생");
        }
    }

    /** ==================== 게시글 수정 ==================== */
    @Override
    @Transactional
    public Integer update(QnABoardDTO dto, Integer requestUserId) {
        try {
            validateBoardDTO(dto);

            if (dto.getQnaBoardId() == null)
                throw new IllegalArgumentException("게시글 ID가 필수입니다.");
            if (requestUserId == null)
                throw new IllegalArgumentException("요청 사용자 ID가 없습니다.");

            QnABoardVO existing = selectById(dto.getQnaBoardId());

            if (!existing.getUserId().equals(requestUserId))
                throw new IllegalStateException("본인 게시글만 수정할 수 있습니다.");

            QnABoardVO vo = QnABoardVO.builder()
                    .qnaBoardId(dto.getQnaBoardId())
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .build();

            int result = qnABoardMapper.update(vo);
            if (result == 0)
                throw new RuntimeException("게시글 수정 실패");

            return result;

        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            log.warn("❌ 수정 오류: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ 수정 중 오류", e);
            throw new RuntimeException("게시글 수정 중 서버 오류 발생");
        }
    }

    /** ==================== 게시글 삭제 ==================== */
    @Override
    @Transactional
    public Integer delete(Integer qnaBoardId, Integer requestUserId) {
        try {
            if (qnaBoardId == null)
                throw new IllegalArgumentException("게시글 ID가 필요합니다.");
            if (requestUserId == null)
                throw new IllegalArgumentException("요청 사용자 ID가 없습니다.");

            QnABoardVO existing = selectById(qnaBoardId);

            if (!existing.getUserId().equals(requestUserId))
                throw new IllegalStateException("본인 게시글만 삭제할 수 있습니다.");

            int result = qnABoardMapper.delete(qnaBoardId);
            if (result == 0)
                throw new RuntimeException("게시글 삭제 실패");

            return result;

        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            log.warn("❌ 삭제 오류: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ 삭제 중 오류", e);
            throw new RuntimeException("게시글 삭제 중 서버 오류 발생");
        }
    }

    /** ==================== 공통 검증 ==================== */
    private void validateBoardDTO(QnABoardDTO dto) {
        if (dto == null)
            throw new IllegalArgumentException("게시글 데이터가 없습니다.");
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("제목은 필수입니다.");
        if (dto.getContent() == null || dto.getContent().trim().isEmpty())
            throw new IllegalArgumentException("내용은 필수입니다.");
    }
}
