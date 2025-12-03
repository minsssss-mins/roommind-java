package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.FileVO;
import com.roomgenius.furniture_recommendation.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    // application.yml → file.upload-dir: ${UPLOAD_DIR:uploads}
    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    /** 실제 사용하는 업로드 루트 경로 */
    private String getUploadRoot() {

        // EC2에서는 /home/ubuntu/uploads 로 들어옴
        // 로컬에서는 uploadDir = "uploads" 이므로 절대경로로 변환 필요
        if (!uploadDir.startsWith("/")) {
            return System.getProperty("user.dir") + "/" + uploadDir;
        }
        return uploadDir;
    }

    /** 파일 검증 */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드된 파일이 비어 있습니다.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 용량은 최대 5MB까지 업로드할 수 있습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("파일 이름이 비어 있습니다.");
        }

        String lower = originalFilename.toLowerCase();
        int dot = lower.lastIndexOf('.');
        if (dot < 0)
            throw new IllegalArgumentException("확장자가 없는 파일은 업로드할 수 없습니다.");

        String ext = lower.substring(dot + 1);

        if (!(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")
                || ext.equals("webp") || ext.equals("gif") || ext.equals("avif"))) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }
    }

    /** 실제 파일 저장 + DB 저장 */
    private List<FileVO> saveFiles(
            Integer qnaBoardId,
            Integer productId,
            Integer communityBoardId,
            List<MultipartFile> files
    ) {

        List<FileVO> savedFiles = new ArrayList<>();
        if (files == null || files.isEmpty()) {
            return savedFiles;
        }

        String today = LocalDate.now().toString();

        // 저장 폴더 타입 결정
        String typeFolder = "common";
        if (qnaBoardId != null) typeFolder = "qna";
        else if (productId != null) typeFolder = "product";
        else if (communityBoardId != null) typeFolder = "community";

        // 실제 저장 루트
        String root = getUploadRoot();
        String realDir = root + "/" + typeFolder + "/" + today;

        File dir = new File(realDir);
        if (!dir.exists() && dir.mkdirs()) {
            log.info("📂 업로드 디렉토리 생성: {}", dir.getAbsolutePath());
        }

        // DB 저장 경로 (URL 경로)
        String dbDir = "uploads/" + typeFolder + "/" + today;

        for (MultipartFile file : files) {
            validateFile(file);

            String uuid = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            String storedFileName = uuid + "_" + originalFilename;

            File dest = new File(realDir, storedFileName);

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
                    .saveDir(dbDir)
                    .fileName(storedFileName)
                    .fileType(0)
                    .fileSize(file.getSize())
                    .build();

            fileMapper.insert(vo);
            savedFiles.add(vo);
            log.info("✅ 파일 저장 완료: {}", vo);
        }

        return savedFiles;
    }

    /** 물리 파일 삭제 */
    private void deletePhysicalFile(String saveDir, String fileName) {
        try {
            //  SEED 이미지 보호: 실제 파일 삭제 금지
            if (saveDir.contains("/seed/")) {
                log.info(" SEED 이미지이므로 실제 파일은 삭제하지 않습니다: {}/{}", saveDir, fileName);
                return;
            }

            String root = getUploadRoot();
            // DB: uploads/product/2025-02-12 → 실제: /home/ubuntu/uploads/product/2025-02-12
            String realPath = root + saveDir.replace("uploads", "");

            File file = new File(realPath, fileName);

            if (file.exists() && file.delete()) {
                log.info("🗑 실제 파일 삭제 완료: {}", file.getAbsolutePath());
            } else {
                log.warn("⚠ 삭제 실패 또는 파일 없음: {}", file.getAbsolutePath());
            }

        } catch (Exception e) {
            log.error("❌ 파일 삭제 중 오류", e);
        }
    }

    // ================== QnA ==================
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
        for (FileVO file : files) deletePhysicalFile(file.getSaveDir(), file.getFileName());
        fileMapper.deleteByQnaBoardId(qnaBoardId);
    }

    // ================== PRODUCT ==================
    @Override
    @Transactional
    public List<FileVO> uploadProductFiles(Integer productId, List<MultipartFile> files) {
        return saveFiles(null, productId, null, files);
    }

    @Override
    public List<FileVO> getProductFiles(Integer productId) {
        return fileMapper.selectByProductId(productId);
    }

    @Override
    @Transactional
    public void deleteProductFiles(Integer productId) {
        List<FileVO> files = fileMapper.selectByProductId(productId);
        for (FileVO file : files) deletePhysicalFile(file.getSaveDir(), file.getFileName());
        fileMapper.deleteByProductId(productId);
    }

    // ================== COMMUNITY ==================
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
        for (FileVO file : files) deletePhysicalFile(file.getSaveDir(), file.getFileName());
        fileMapper.deleteByCommunityBoardId(communityBoardId);
    }

    // ================== 공통 ==================
    @Override
    public FileVO getFileByUuid(String uuid) {
        return fileMapper.selectByUuid(uuid);
    }

    @Override
    @Transactional
    public void deleteFileByUuid(String uuid) {
        FileVO file = fileMapper.selectByUuid(uuid);
        if (file != null)
            deletePhysicalFile(file.getSaveDir(), file.getFileName());
        fileMapper.deleteByUuid(uuid);
    }
}
