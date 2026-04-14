package com.sweet_hjr.server.domain.upload.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurationGroupSummaryResponse {
    private Long imageGroupId;
    private String groupKey;
    private Long representativeImageId;
    private String representativeImageUrl;
    private int itemCount;
}