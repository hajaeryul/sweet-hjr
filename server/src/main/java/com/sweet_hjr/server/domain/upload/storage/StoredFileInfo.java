package com.sweet_hjr.server.domain.upload.storage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoredFileInfo {

    private String originalFileName;
    private String storedFileName;
    private String fileUrl;
    private String mimeType;
    private Long fileSize;
    private Integer width;
    private Integer height;
}