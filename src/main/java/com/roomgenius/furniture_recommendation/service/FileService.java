package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    // ===== QnA 게시글용 =====
    /**
     * QnA 게시글에 파일 업로드
     * @param qnaBoardId QnA 게시글 ID
     * @param files 업로드할 파일 목록
     * @return 업로드된 파일 목록
     */
    List<FileVO> uploadQnaFiles(Integer qnaBoardId, List<MultipartFile> files);

    List<FileVO> getQnaFiles(Integer qnaBoardId);

    void deleteQnaFiles(Integer qnaBoardId);


    // ===== 상품(Product)용 =====
    List<FileVO> uploadProductFiles(Integer productId, List<MultipartFile> files);

    List<FileVO> getProductFiles(Integer productId);

    void deleteProductFiles(Integer productId);


    // ===== 커뮤니티 게시글용 =====
    List<FileVO> uploadCommunityFiles(Integer communityBoardId, List<MultipartFile> files);

    List<FileVO> getCommunityFiles(Integer communityBoardId);

    void deleteCommunityFiles(Integer communityBoardId);


    // ===== 공통 유틸 =====
    FileVO getFileByUuid(String uuid);

    void deleteFileByUuid(String uuid);
}