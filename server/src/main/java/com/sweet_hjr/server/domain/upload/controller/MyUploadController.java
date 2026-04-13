package com.sweet_hjr.server.domain.upload.controller;

import com.sweet_hjr.server.domain.upload.dto.MyFanUploadResponse;
import com.sweet_hjr.server.domain.upload.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/uploads")
public class MyUploadController {

    private final UploadService uploadService;

    @GetMapping("/my")
    public List<MyFanUploadResponse> getMyUploads(
            @RequestParam Long userId
    ) {
        return uploadService.getMyUploads(userId);
    }
}