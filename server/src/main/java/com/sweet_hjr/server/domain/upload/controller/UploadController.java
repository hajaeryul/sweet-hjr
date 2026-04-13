package com.sweet_hjr.server.domain.upload.controller;

import com.sweet_hjr.server.domain.upload.dto.*;
import com.sweet_hjr.server.domain.upload.service.UploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/uploads")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateFanUploadResponse createFanUpload(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateFanUploadRequest request
    ) {
        return uploadService.createFanUpload(projectId, request);
    }

    @PostMapping("/{fanUploadId}/files")
    public UploadFilesResponse uploadFiles(
            @PathVariable Long projectId,
            @PathVariable Long fanUploadId,
            @RequestParam Long userId,
            @RequestPart MultipartFile[] files
    ) throws IOException {
        return uploadService.uploadFiles(projectId, fanUploadId, userId, files);
    }

    @PatchMapping("/{fanUploadId}/complete")
    public CompleteFanUploadResponse completeFanUpload(
            @PathVariable Long projectId,
            @PathVariable Long fanUploadId,
            @Valid @RequestBody CompleteFanUploadRequest request
    ) {
        return uploadService.completeFanUpload(projectId, fanUploadId, request);
    }

    @GetMapping("/files")
    public List<UploadedFileItemResponse> getProjectUploadFiles(
            @PathVariable Long projectId
    ) {
        return uploadService.getProjectUploadFiles(projectId);
    }
}