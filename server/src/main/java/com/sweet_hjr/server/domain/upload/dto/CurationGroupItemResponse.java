package com.sweet_hjr.server.domain.upload.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CurationGroupItemResponse {
    private Long fileId;
    private Long userId;
    private String userNickname;
    private String originalFileName;
    private String fileUrl;
    private Integer width;
    private Integer height;
    private Long fileSize;

    private String aiReviewStatus;
    private String aiReviewSummary;

    private BigDecimal resolutionScore;
    private BigDecimal brightnessScore;
    private BigDecimal blurScore;
    private BigDecimal noiseScore;

    private Boolean watermarkDetected;
    private Boolean textDetected;
    private Boolean inappropriateFlag;
}