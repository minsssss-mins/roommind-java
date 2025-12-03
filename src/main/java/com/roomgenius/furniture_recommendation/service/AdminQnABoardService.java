package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.QnABoardVO;
import java.util.List;

public interface AdminQnABoardService {

    /** 전체 Q&A 목록 조회 */
    List<QnABoardVO> selectAll();

    /** 특정 Q&A 단일 조회 */
    QnABoardVO selectById(Integer qnaId);

    /** 관리자 답변 등록 또는 수정 */
    void updateAnswer(Integer qnaId, String answer);

    /** Q&A 게시글 삭제 */
    void delete(Integer qnaId);
}
