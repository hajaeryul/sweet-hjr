package com.sweet_hjr.server.domain.upload.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UploadFilesResponse {

    private Long fanUploadId;
    private int uploadedCount;
    private List<UploadedFileItemResponse> files;
}