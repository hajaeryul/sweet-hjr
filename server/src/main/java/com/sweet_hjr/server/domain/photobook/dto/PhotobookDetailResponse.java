package com.sweet_hjr.server.domain.photobook.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PhotobookDetailResponse {
    private Long photobookId;
    private Long projectId;
    private String title;
    private String bookSpecUid;
    private String sweetbookBookUid;
    private String status;
    private Integer finalPageCount;
    private String lastErrorMessage;
    private List<PhotobookPagePreviewResponse> pages;
}