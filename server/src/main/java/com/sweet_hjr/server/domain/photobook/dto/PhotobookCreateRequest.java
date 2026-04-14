package com.sweet_hjr.server.domain.photobook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PhotobookCreateRequest {

    private Long adminUserId;
    private String title;
    private String bookSpecUid;
    private String specProfileUid;
    private String coverTemplateUid;
    private String contentTemplateUid;
    private boolean uploadToSweetbook;

    public String buildExternalRef(Long projectId) {
        return "project-" + projectId + "-photobook";
    }
}