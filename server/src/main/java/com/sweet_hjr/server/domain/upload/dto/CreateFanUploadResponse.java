package com.sweet_hjr.server.domain.upload.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateFanUploadResponse {

    private Long fanUploadId;
    private Long projectId;
    private Long userId;
    private String status;
    private String memo;
}