package com.sweet_hjr.server.domain.upload.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CurateImageRequest {

    @NotNull
    private Long sourceImageId;

    @NotNull
    private Long imageGroupId;

    @NotNull
    private Long selectedBy;

    private String selectedReason;

    @NotNull
    private Integer priorityOrder;
}