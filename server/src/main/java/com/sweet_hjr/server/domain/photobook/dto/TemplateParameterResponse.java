package com.sweet_hjr.server.domain.photobook.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TemplateParameterResponse {
    private String key;
    private String binding;
    private Boolean required;
    private String type;
    private String label;
    private String description;
}