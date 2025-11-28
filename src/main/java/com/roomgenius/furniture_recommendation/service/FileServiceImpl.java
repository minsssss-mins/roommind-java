package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.FileVO;
import com.roomgenius.furniture_recommendation.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    // TODO: 나중에 application.yml로 빼도 좋음
    private static final String UPLOAD_ROOT = System.getProperty("user.dir") + "/uploads";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    // ================== 공통 내부 메서드 ==================

    /** 파일 검증: 확장자, MIME, 사이즈 */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드된 파일이 비어 있습니다.");
        }

        // 1) 사이즈 체크
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 용량은 최대 5MB까지 업로드할 수 있습니다.");
        }

        // 2) 파일명 + 확장자 체크
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("파일 이름이 비어 있습니다.");
        }

        String lower = originalFilename.toLowerCase();
        int dot = lower.lastIndexOf('.');
        if (dot < 0) {
            throw new IllegalArgumentException("확장자가 없는 파일은 업로드할 수 없습니다.");
        }

        String ext = lower.substring(dot + 1);
        if (!(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")
                || ext.equals("webp") || ext.equals("gif") || ext.equals("avif"))) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다. (jpg, jpeg, png, webp, gif,avif)");
        }

        // 3) MIME 타입 체크
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 형식의 파일만 업로드할 수 있습니다. (Content-Type: " + contentType + ")");
        }
    }

    /** 실제 파일 저장 + DB insert */
    /** 실제 파일 저장 + DB insert */
    private List<FileVO> saveFiles(
            Integer qnaBoardId,
            Integer productId,
            Integer communityBoardId,
            List<MultipartFile> files) {

        List<FileVO> savedFiles = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            return savedFiles;
        }

        // 오늘 날짜
        String today = LocalDate.now().toString();

        // ⭐ 게시판 유형별 하위 폴더 결정
        String typeFolder = "common";

        if (qnaBoardId != null)         typeFolder = "qna";
        else if (productId != null)     typeFolder = "product";
        else if (communityBoardId != null) typeFolder = "community";

        // 폴더 구조: uploads/{type}/{날짜}/
        String saveDir = UPLOAD_ROOT + "/" + typeFolder + "/" + today;

        File dir = new File(saveDir);
        if (!dir.exists() && dir.mkdirs()) {
            log.info("📂 업로드 디렉토리 생성: {}", dir.getAbsolutePath());
        }

        for (MultipartFile file : files) {
            validateFile(file);

            String originalFilename = file.getOriginalFilename();
            long fileSize = file.getSize();
            String uuid = UUID.randomUUID().toString();

            String storedFileName = uuid + "_" + originalFilename;
            File dest = new File(dir, storedFileName);

            try {
                file.transferTo(dest);
            } catch (IOException e) {
                log.error("❌ 파일 저장 실패: {}", dest.getAbsolutePath(), e);
                throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
            }

            FileVO vo = FileVO.builder()
                    .uuid(uuid)
                    .qnaBoardId(qnaBoardId)
                    .productId(productId)
                    .communityBoardId(communityBoardId)
                    .saveDir(saveDir)
                    .fileName(storedFileName)
                    .fileType(0)
                    .fileSize(fileSize)
                    .build();

            fileMapper.insert(vo);
            savedFiles.add(vo);
            log.info("✅ 파일 메타데이터 저장 완료: {}", vo);
        }

        return savedFiles;
    }


    /** 물리 파일 삭제 */
    private void deletePhysicalFile(String saveDir, String fileName) {
        try {
            File file = new File(saveDir, fileName);
            if (file.exists() && file.delete()) {
                log.info("✅ 물리 파일 삭제 완료: {}", file.getAbsolutePath());
            } else {
                log.warn("⚠ 물리 파일 삭제 실패: {}", file.getAbsolutePath());
            }
        } catch (Exception e) {
            log.error("❌ 파일 삭제 중 오류: ", e);
        }
    }





    // ================== QnA용 구현 ==================

    @Override
    @Transactional
    public List<FileVO> uploadQnaFiles(Integer qnaBoardId, List<MultipartFile> files) {
        return saveFiles(qnaBoardId, null, null, files);
    }

    @Override
    public List<FileVO> getQnaFiles(Integer qnaBoardId) {
        return fileMapper.selectByQnaBoardId(qnaBoardId);
    }

    @Override
    @Transactional
    public void deleteQnaFiles(Integer qnaBoardId) {
        List<FileVO> files = fileMapper.selectByQnaBoardId(qnaBoardId);
        for (FileVO file : files) {
            deletePhysicalFile(file.getSaveDir(), file.getFileName());
        }
        fileMapper.deleteByQnaBoardId(qnaBoardId);
    }

    // ================== 상품(Product)용 구현 ==================

    // 이미지 등록
    @Override
    @Transactional
    public List<FileVO> uploadProductFiles(Integer productId, List<MultipartFile> files) {
        return saveFiles(null, productId, null, files);
    }

    // 이미지 조회
    @Override
    public List<FileVO> getProductFiles(Integer productId) {
        return fileMapper.selectByProductId(productId);
    }

    // 이미지 삭제
    @Override
    @Transactional
    public void deleteProductFiles(Integer productId) {
        List<FileVO> files = fileMapper.selectByProductId(productId);
        for (FileVO file : files) {
            deletePhysicalFile(file.getSaveDir(), file.getFileName());
        }
        fileMapper.deleteByProductId(productId);
    }

    // ================== 커뮤니티용 구현 ==================

    @Override
    @Transactional
    public List<FileVO> uploadCommunityFiles(Integer communityBoardId, List<MultipartFile> files) {
        return saveFiles(null, null, communityBoardId, files);
    }

    @Override
    public List<FileVO> getCommunityFiles(Integer communityBoardId) {
        return fileMapper.selectByCommunityBoardId(communityBoardId);
    }

    @Override
    @Transactional
    public void deleteCommunityFiles(Integer communityBoardId) {
        List<FileVO> files = fileMapper.selectByCommunityBoardId(communityBoardId);
        for (FileVO file : files) {
            deletePhysicalFile(file.getSaveDir(), file.getFileName());
        }
        fileMapper.deleteByCommunityBoardId(communityBoardId);
    }

    // ================== 공통 구현 ==================

    @Override
    public FileVO getFileByUuid(String uuid) {
        return fileMapper.selectByUuid(uuid);
    }

    @Override
    @Transactional
    public void deleteFileByUuid(String uuid) {
        FileVO file = fileMapper.selectByUuid(uuid);
        if (file != null) {
            deletePhysicalFile(file.getSaveDir(), file.getFileName());
        }
        fileMapper.deleteByUuid(uuid);
    }
}