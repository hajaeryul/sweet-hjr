package com.sweet_hjr.server.domain.upload.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${app.upload.base-dir:uploads}")
    private String baseDir;

    @Override
    public StoredFileInfo store(Long projectId, Long fanUploadId, MultipartFile file) throws IOException {
        validateImageFile(file);

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = extractExtension(originalFileName);
        String storedFileName = UUID.randomUUID() + (extension.isBlank() ? "" : "." + extension);

        Path uploadDir = Paths.get(baseDir, "projects", String.valueOf(projectId), String.valueOf(fanUploadId));
        Files.createDirectories(uploadDir);

        Path targetPath = uploadDir.resolve(storedFileName);
        file.transferTo(targetPath.toFile());

        BufferedImage image = ImageIO.read(targetPath.toFile());
        if (image == null) {
            Files.deleteIfExists(targetPath);
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        return StoredFileInfo.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .fileUrl("/uploads/projects/" + projectId + "/" + fanUploadId + "/" + storedFileName)
                .mimeType(file.getContentType())
                .fileSize(file.getSize())
                .width(image.getWidth())
                .height(image.getHeight())
                .build();
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("비어 있는 파일은 업로드할 수 없습니다.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }
    }

    private String extractExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex < 0 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }
}