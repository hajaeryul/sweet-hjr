package com.sweet_hjr.server.domain.upload.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompleteFanUploadResponse {

    private Long fanUploadId;
    private String status;
    private long totalFileCount;
}