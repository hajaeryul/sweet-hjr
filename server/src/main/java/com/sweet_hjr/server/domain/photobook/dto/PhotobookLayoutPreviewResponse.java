package com.sweet_hjr.server.domain.photobook.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PhotobookLayoutPreviewResponse {
    private Long projectId;
    private Integer contentPageCount;
    private Integer expectedFinalPageCount;
    private PhotobookPagePreviewResponse cover;
    private List<PhotobookPagePreviewResponse> pages;
}