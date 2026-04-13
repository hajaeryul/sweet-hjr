package com.sweet_hjr.server.domain.project.dto;

import com.sweet_hjr.server.domain.project.entity.Project;
import com.sweet_hjr.server.domain.project.entity.ProjectStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProjectResponse {

    private Long id;
    private Long influencerId;
    private Long createdBy;
    private String title;
    private String description;
    private String coverImageUrl;
    private ProjectStatus status;
    private LocalDateTime uploadStartAt;
    private LocalDateTime uploadEndAt;
    private LocalDateTime previewStartAt;
    private LocalDateTime previewEndAt;
    private LocalDateTime orderStartAt;
    private LocalDateTime orderEndAt;

    public static ProjectResponse from(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .influencerId(project.getInfluencer().getId())
                .createdBy(project.getCreatedBy().getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .coverImageUrl(project.getCoverImageUrl())
                .status(project.getStatus())
                .uploadStartAt(project.getUploadStartAt())
                .uploadEndAt(project.getUploadEndAt())
                .previewStartAt(project.getPreviewStartAt())
                .previewEndAt(project.getPreviewEndAt())
                .orderStartAt(project.getOrderStartAt())
                .orderEndAt(project.getOrderEndAt())
                .build();
    }
}