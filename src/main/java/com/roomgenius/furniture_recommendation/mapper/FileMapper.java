package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.FileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper {

    // 1) 파일 단건 등록
    int insert(FileVO fileVO);

    // 2) uuid로 단건 조회
    FileVO selectByUuid(@Param("uuid") String uuid);

    // 3) QnA 게시글에 연결된 파일 목록 조회
    List<FileVO> selectByQnaBoardId(@Param("qnaBoardId") Integer qnaBoardId);

    // 4) 상품에 연결된 파일 목록 조회
    List<FileVO> selectByProductId(@Param("productId") Integer productId);

    // 5) 커뮤니티 게시글에 연결된 파일 목록 조회
    List<FileVO> selectByCommunityBoardId(@Param("communityBoardId") Integer communityBoardId);

    // 6) uuid로 단건 삭제
    int deleteByUuid(@Param("uuid") String uuid);

    // 7) QnA 게시글에 연결된 파일 전체 삭제
    int deleteByQnaBoardId(@Param("qnaBoardId") Integer qnaBoardId);

    // 8) 상품에 연결된 파일 전체 삭제
    int deleteByProductId(@Param("productId") Integer productId);

    // 9) 커뮤니티 게시글에 연결된 파일 전체 삭제
    int deleteByCommunityBoardId(@Param("communityBoardId") Integer communityBoardId);
}
