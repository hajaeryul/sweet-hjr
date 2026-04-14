package com.sweet_hjr.server.domain.upload.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CuratedImagePreviewResponse {
    private Long curatedImageId;
    private Long sourceImageId;
    private String fileUrl;
    private Integer priorityOrder;
    private String selectedReason;
}