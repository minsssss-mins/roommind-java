package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.QnABoardVO;
import com.roomgenius.furniture_recommendation.mapper.QnABoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminQnABoardServiceImpl implements AdminQnABoardService {

    private final QnABoardMapper qnABoardMapper;

    /** 전체 조회 */
    @Override
    public List<QnABoardVO> selectAll() {
        return qnABoardMapper.selectAll();
    }

    /** 단일 조회 */
    @Override
    public QnABoardVO selectById(Integer qnaId) {
        return qnABoardMapper.selectById(qnaId);
    }

    /** 관리자 답변 등록/수정 */
    @Override
    public void updateAnswer(Integer qnaId, String answer) {
        qnABoardMapper.updateAnswer(qnaId, answer);
    }

    /** 관리자 삭제 */
    @Override
    public void delete(Integer qnaId) {
        qnABoardMapper.deleteAdmin(qnaId);
    }
}
