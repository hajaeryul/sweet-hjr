package com.sweet_hjr.server.domain.photobook.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhotobookPagePreviewResponse {
    private Long curatedImageId;
    private Integer pageNumber;
    private String pageRole;
    private String imageUrl;
    private String caption;
    private Integer priorityOrder;
}