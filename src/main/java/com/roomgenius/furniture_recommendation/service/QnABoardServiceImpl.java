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

@Slf4j
@RequiredArgsConstructor
@Service
public class QnABoardServiceImpl implements QnABoardService {

    private final QnABoardMapper qnABoardMapper;
    private final UserMapper userMapper;

    /**
     * ✅ 게시글 등록 (이미지 X, 순수 QnA 데이터만)
     */
    @Override
    @Transactional
    public Integer insert(QnABoardDTO dto) {
        try {
            log.info("📌 QnA 게시글 등록 요청: {}", dto);
            validateBoardDTO(dto);

            // userId로 사용자 존재 확인
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

            Integer result = qnABoardMapper.insert(vo);
            if (result == 0) {
                throw new RuntimeException("게시글 등록 실패");
            }

            log.info("✅ QnA 게시글 등록 완료: {}", vo);
            return result;

        } catch (IllegalArgumentException e) {
            log.warn("❌ 잘못된 요청: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ 게시글 등록 중 오류: {}", e.getMessage(), e);
            throw new RuntimeException("게시글 등록 중 오류 발생", e);
        }
    }

    /**
     * ✅ 게시글 전체 조회
     */
    @Override
    public List<QnABoardVO> selectAll() {
        log.info("📌 QnA 게시글 전체 조회");
        return qnABoardMapper.selectAll();
    }

    /**
     * ✅ 게시글 상세 조회
     */
    @Override
    public QnABoardVO selectById(Integer qnaBoardId) {
        log.info("📌 QnA 게시글 상세 조회 요청: {}", qnaBoardId);

        if (qnaBoardId == null) {
            throw new IllegalArgumentException("게시글 ID가 필수입니다.");
        }

        QnABoardVO vo = qnABoardMapper.selectById(qnaBoardId);
        if (vo == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        return vo;
    }

    /**
     * ✅ 게시글 수정 (이미지 X)
     */
    @Override
    @Transactional
    public Integer update(QnABoardDTO dto) {
        try {
            log.info("📌 QnA 게시글 수정 요청: {}", dto);
            validateBoardDTO(dto);

            if (dto.getQnaBoardId() == null) {
                throw new IllegalArgumentException("게시글 ID가 필수입니다.");
            }

            QnABoardVO existing = qnABoardMapper.selectById(dto.getQnaBoardId());
            if (existing == null) {
                throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
            }

            QnABoardVO vo = QnABoardVO.builder()
                    .qnaBoardId(dto.getQnaBoardId())
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .build();

            Integer result = qnABoardMapper.update(vo);
            if (result == 0) {
                throw new RuntimeException("게시글 수정 실패");
            }

            log.info("✅ QnA 게시글 수정 성공: {}", vo);
            return result;

        } catch (IllegalArgumentException e) {
            log.warn("❌ 잘못된 요청: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ 게시글 수정 중 오류: {}", e.getMessage(), e);
            throw new RuntimeException("게시글 수정 중 오류 발생", e);
        }
    }

    /**
     * ✅ 게시글 삭제
     * - DB FK(File.qna_board_id ON DELETE CASCADE) 때문에
     *   연관된 File 레코드는 자동 삭제됨 (물리 파일은 FileService에서 처리)
     */
    @Override
    @Transactional
    public Integer delete(Integer qnaBoardId) {
        log.info("📌 QnA 게시글 삭제 요청: {}", qnaBoardId);

        if (qnaBoardId == null) {
            throw new IllegalArgumentException("게시글 ID가 필수입니다.");
        }

        Integer result = qnABoardMapper.delete(qnaBoardId);
        if (result == 0) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        log.info("✅ QnA 게시글 삭제 완료: {}", qnaBoardId);
        return result;
    }

    /**
     * ✅ 입력값 검증
     */
    private void validateBoardDTO(QnABoardDTO dto) {
        if (dto == null) throw new IllegalArgumentException("게시글 데이터가 없습니다.");
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("제목은 필수입니다.");
        if (dto.getContent() == null || dto.getContent().trim().isEmpty())
            throw new IllegalArgumentException("내용은 필수입니다.");
    }
}