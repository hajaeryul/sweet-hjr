package com.sweet_hjr.server.domain.photobook.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TemplateSummaryResponse {
    private String templateUid;
    private String templateName;
    private String description;
    private String theme;
    private String templateKind;
    private String bookSpecUid;
    private String status;
    private String layoutThumbnailUrl;
    private Integer requiredParameterCount;
}