package com.sweet_hjr.server.domain.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProjectCreateRequest {

    @NotNull
    private Long influencerId;

    @NotNull
    private Long createdByUserId;

    @NotBlank
    private String title;

    private String description;
    private String coverImageUrl;

    @NotNull
    private LocalDateTime uploadStartAt;

    @NotNull
    private LocalDateTime uploadEndAt;

    private LocalDateTime previewStartAt;
    private LocalDateTime previewEndAt;
    private LocalDateTime orderStartAt;
    private LocalDateTime orderEndAt;
}