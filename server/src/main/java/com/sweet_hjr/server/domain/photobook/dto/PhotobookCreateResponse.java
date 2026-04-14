package com.sweet_hjr.server.domain.photobook.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhotobookCreateResponse {
    private Long photobookId;
    private Long projectId;
    private String sweetbookBookUid;
    private String status;
    private Integer finalPageCount;
}