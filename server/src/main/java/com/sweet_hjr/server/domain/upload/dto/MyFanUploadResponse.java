package com.sweet_hjr.server.domain.upload.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MyFanUploadResponse {

    private Long fanUploadId;
    private Long projectId;
    private String projectTitle;
    private String status;
    private String memo;
    private LocalDateTime createdAt;
    private List<MyUploadFileResponse> files;
}