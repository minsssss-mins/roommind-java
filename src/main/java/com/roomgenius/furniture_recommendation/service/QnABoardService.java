package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.QnABoardDTO;
import com.roomgenius.furniture_recommendation.entity.QnABoardVO;

import java.util.List;

/**
 * QnA 게시판 서비스 인터페이스
 * - 이미지 로직은 FileService에서 따로 처리
 */
public interface QnABoardService {

    /**
     * 게시글 등록
     * @param dto 게시글 정보
     * @return 영향받은 행 수
     */
    Integer insert(QnABoardDTO dto);

    /**
     * 게시글 전체 조회
     * @return 게시글 목록
     */
    List<QnABoardVO> selectAll();

    /**
     * 게시글 상세 조회
     * @param qnaBoardId 게시글 ID
     * @return 게시글 상세 정보
     */
    QnABoardVO selectById(Integer qnaBoardId);

    /**
     * 게시글 수정
     * @param dto 수정할 게시글 정보 (qnaBoardId 필수)
     * @return 영향받은 행 수
     */
    Integer update(QnABoardDTO dto);

    /**
     * 게시글 삭제
     * @param qnaBoardId 게시글 ID
     * @return 영향받은 행 수
     */
    Integer delete(Integer qnaBoardId);
}