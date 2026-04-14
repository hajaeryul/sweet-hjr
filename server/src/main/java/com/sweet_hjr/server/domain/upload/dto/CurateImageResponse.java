package com.sweet_hjr.server.domain.upload.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurateImageResponse {
    private Long curatedImageId;
    private Long projectId;
    private Long sourceImageId;
    private Long imageGroupId;
    private String sourceType;
    private Integer priorityOrder;
}