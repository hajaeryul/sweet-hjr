package com.sweet_hjr.server.domain.upload.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CompleteFanUploadRequest {

    @NotNull
    private Long userId;
}