package com.sweet_hjr.server.domain.photobook.service;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class PhotobookLayoutModels {

    @Getter
    @Builder
    public static class LayoutResult {
        private Long projectId;
        private LayoutPage cover;
        private List<LayoutPage> contents;
        private Integer expectedFinalPageCount;
    }

    @Getter
    @Builder
    public static class LayoutPage {
        private Long curatedImageId;
        private Long sourceImageId;
        private Integer pageNumber;
        private String pageRole;
        private String imageUrl;
        private String caption;
        private Integer priorityOrder;
    }
}