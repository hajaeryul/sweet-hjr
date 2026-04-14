package com.sweet_hjr.server.domain.photobook.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TemplateDetailResponse {
    private String templateUid;
    private String templateName;
    private String description;
    private String theme;
    private String templateKind;
    private String bookSpecUid;
    private String status;
    private String layoutThumbnailUrl;
    private String mockupThumbnailUrl;
    private Integer requiredParameterCount;
    private List<TemplateParameterResponse> parameters;
}