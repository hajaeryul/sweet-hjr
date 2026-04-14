package com.sweet_hjr.server.domain.upload.service;

import com.sweet_hjr.server.domain.project.entity.Project;
import com.sweet_hjr.server.domain.project.repository.ProjectRepository;
import com.sweet_hjr.server.domain.upload.dto.*;
import com.sweet_hjr.server.domain.upload.entity.FanUpload;
import com.sweet_hjr.server.domain.upload.entity.FanUploadFile;
import com.sweet_hjr.server.domain.upload.entity.UploadStatus;
import com.sweet_hjr.server.domain.upload.repository.FanUploadFileRepository;
import com.sweet_hjr.server.domain.upload.repository.FanUploadRepository;
import com.sweet_hjr.server.domain.upload.storage.FileStorageService;
import com.sweet_hjr.server.domain.upload.storage.StoredFileInfo;
import com.sweet_hjr.server.domain.user.entity.User;
import com.sweet_hjr.server.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {

    private final FanUploadRepository fanUploadRepository;
    private final FanUploadFileRepository fanUploadFileRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final ImageAnalysisService imageAnalysisService;
    private final ImageGroupingService imageGroupingService;

    public CreateFanUploadResponse createFanUpload(Long projectId, CreateFanUploadRequest request) {
        Project project = getProject(projectId);
        User user = getUser(request.getUserId());

        validateProjectUploadWindow(project);

        FanUpload fanUpload = FanUpload.builder()
                .project(project)
                .user(user)
                .status(UploadStatus.PENDING)
                .memo(request.getMemo())
                .build();

        FanUpload saved = fanUploadRepository.save(fanUpload);

        return CreateFanUploadResponse.builder()
                .fanUploadId(saved.getId())
                .projectId(project.getId())
                .userId(user.getId())
                .status(saved.getStatus().name())
                .memo(saved.getMemo())
                .build();
    }

    public UploadFilesResponse uploadFiles(
            Long projectId,
            Long fanUploadId,
            Long userId,
            MultipartFile[] files
    ) throws IOException {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        Project project = getProject(projectId);
        validateProjectUploadWindow(project);

        FanUpload fanUpload = fanUploadRepository.findByIdAndProjectIdAndUserId(fanUploadId, projectId, userId)
                .orElseThrow(() -> new EntityNotFoundException("업로드 세션을 찾을 수 없습니다."));

        User user = getUser(userId);

        List<UploadedFileItemResponse> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            StoredFileInfo storedFile = fileStorageService.store(projectId, fanUploadId, file);

            FanUploadFile fanUploadFile = FanUploadFile.builder()
                    .fanUpload(fanUpload)
                    .project(project)
                    .user(user)
                    .originalFileName(storedFile.getOriginalFileName())
                    .fileUrl(storedFile.getFileUrl())
                    .thumbnailUrl(null)
                    .mimeType(storedFile.getMimeType())
                    .fileSize(storedFile.getFileSize())
                    .width(storedFile.getWidth())
                    .height(storedFile.getHeight())
                    .uploadedAt(LocalDateTime.now())
                    .build();

            FanUploadFile savedFile = fanUploadFileRepository.save(fanUploadFile);

            uploadedFiles.add(
                    UploadedFileItemResponse.builder()
                            .fileId(savedFile.getId())
                            .originalFileName(savedFile.getOriginalFileName())
                            .fileUrl(savedFile.getFileUrl())
                            .mimeType(savedFile.getMimeType())
                            .fileSize(savedFile.getFileSize())
                            .width(savedFile.getWidth())
                            .height(savedFile.getHeight())
                            .build()
            );
        }

        return UploadFilesResponse.builder()
                .fanUploadId(fanUploadId)
                .uploadedCount(uploadedFiles.size())
                .files(uploadedFiles)
                .build();
    }

    public CompleteFanUploadResponse completeFanUpload(
            Long projectId,
            Long fanUploadId,
            CompleteFanUploadRequest request
    ) {
        FanUpload fanUpload = fanUploadRepository.findByIdAndProjectIdAndUserId(
                        fanUploadId,
                        projectId,
                        request.getUserId()
                )
                .orElseThrow(() -> new EntityNotFoundException("업로드 세션을 찾을 수 없습니다."));

        List<FanUploadFile> uploadFiles = fanUploadFileRepository.findAllByFanUploadId(fanUploadId);

        if (uploadFiles.isEmpty()) {
            throw new IllegalStateException("업로드된 파일이 없어 완료 처리할 수 없습니다.");
        }

        for (FanUploadFile file : uploadFiles) {
            imageAnalysisService.analyze(file);
        }

        imageGroupingService.regroupProject(projectId);

        fanUpload.changeStatus(UploadStatus.FILTERED);

        return CompleteFanUploadResponse.builder()
                .fanUploadId(fanUpload.getId())
                .status(fanUpload.getStatus().name())
                .totalFileCount(uploadFiles.size())
                .build();
    }

    private Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트를 찾을 수 없습니다. id=" + projectId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. id=" + userId));
    }

    private void validateProjectUploadWindow(Project project) {
        LocalDateTime now = LocalDateTime.now();

        if (project.getUploadStartAt() == null || project.getUploadEndAt() == null) {
            throw new IllegalStateException("업로드 기간이 설정되지 않은 프로젝트입니다.");
        }

        if (now.isBefore(project.getUploadStartAt()) || now.isAfter(project.getUploadEndAt())) {
            throw new IllegalStateException("현재 업로드 가능한 기간이 아닙니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<UploadedFileItemResponse> getProjectUploadFiles(Long projectId) {
        getProject(projectId);

        return fanUploadFileRepository.findAllByProjectIdOrderByUploadedAtDesc(projectId)
                .stream()
                .map(file -> UploadedFileItemResponse.builder()
                        .fileId(file.getId())
                        .fanUploadId(file.getFanUpload().getId())
                        .userId(file.getUser().getId())
                        .userNickname(file.getUser().getNickname())
                        .originalFileName(file.getOriginalFileName())
                        .fileUrl(file.getFileUrl())
                        .mimeType(file.getMimeType())
                        .fileSize(file.getFileSize())
                        .width(file.getWidth())
                        .height(file.getHeight())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MyFanUploadResponse> getMyUploads(Long userId) {
        getUser(userId);

        List<FanUpload> fanUploads = fanUploadRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        if (fanUploads.isEmpty()) {
            return List.of();
        }

        List<Long> fanUploadIds = fanUploads.stream()
                .map(FanUpload::getId)
                .toList();

        List<FanUploadFile> allFiles = fanUploadFileRepository.findAllByFanUploadIdInOrderByUploadedAtDesc(fanUploadIds);

        Map<Long, List<MyUploadFileResponse>> fileMap = allFiles.stream()
                .collect(Collectors.groupingBy(
                        file -> file.getFanUpload().getId(),
                        Collectors.mapping(
                                file -> MyUploadFileResponse.builder()
                                        .fileId(file.getId())
                                        .originalFileName(file.getOriginalFileName())
                                        .fileUrl(file.getFileUrl())
                                        .mimeType(file.getMimeType())
                                        .fileSize(file.getFileSize())
                                        .width(file.getWidth())
                                        .height(file.getHeight())
                                        .build(),
                                Collectors.toList()
                        )
                ));

        return fanUploads.stream()
                .map(upload -> MyFanUploadResponse.builder()
                        .fanUploadId(upload.getId())
                        .projectId(upload.getProject().getId())
                        .projectTitle(upload.getProject().getTitle())
                        .status(upload.getStatus().name())
                        .memo(upload.getMemo())
                        .createdAt(upload.getCreatedAt())
                        .files(fileMap.getOrDefault(upload.getId(), List.of()))
                        .build())
                .toList();
    }
}