package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.QnABoardDTO;
import com.roomgenius.furniture_recommendation.entity.QnABoardVO;

import java.util.List;

public interface QnABoardService {

    /** 게시글 등록 */
    Integer insert(QnABoardDTO dto);

    /** 게시글 전체 조회 */
    List<QnABoardVO> selectAll();

    /** 게시글 상세 조회 */
    QnABoardVO selectById(Integer qnaBoardId);

    /** 게시글 수정 */
    Integer update(QnABoardDTO dto, Integer requestUserId);

    /** 게시글 삭제 */
    Integer delete(Integer qnaBoardId, Integer requestUserId);
}
