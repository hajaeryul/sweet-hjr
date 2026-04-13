package com.sweet_hjr.server.domain.upload.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyUploadFileResponse {

    private Long fileId;
    private String originalFileName;
    private String fileUrl;
    private String mimeType;
    private Long fileSize;
    private Integer width;
    private Integer height;
}