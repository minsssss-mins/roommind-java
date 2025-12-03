package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.QnABoardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QnABoardMapper {

    /** 사용자 - 게시글 등록 */
    int insert(QnABoardVO bvo);

    /** 전체 조회 */
    List<QnABoardVO> selectAll();

    /** 단일 조회 */
    QnABoardVO selectById(@Param("qnaId") Integer qnaId);

    /** 사용자 - 게시글 수정 */
    int update(QnABoardVO bvo);

    /** 사용자 - 게시글 삭제 */
    int delete(@Param("qnaId") Integer qnaId);

    /**  관리자 - 답변 등록/수정 */
    int updateAnswer(
            @Param("qnaId") Integer qnaId,
            @Param("answer") String answer
    );

    /**  관리자 - 문의글 삭제 */
    int deleteAdmin(@Param("qnaId") Integer qnaId);
}
