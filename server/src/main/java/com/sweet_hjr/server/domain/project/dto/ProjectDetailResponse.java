package com.sweet_hjr.server.domain.project.dto;

import com.sweet_hjr.server.domain.project.entity.Project;
import com.sweet_hjr.server.domain.project.entity.ProjectStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProjectDetailResponse {

    private Long id;
    private String title;
    private String description;
    private String coverImageUrl;
    private ProjectStatus status;

    private Long influencerId;
    private String influencerName;
    private String influencerDisplayName;
    private String influencerProfileImageUrl;

    private Long createdByUserId;
    private String createdByNickname;

    private LocalDateTime uploadStartAt;
    private LocalDateTime uploadEndAt;
    private LocalDateTime previewStartAt;
    private LocalDateTime previewEndAt;
    private LocalDateTime orderStartAt;
    private LocalDateTime orderEndAt;

    private boolean uploadAvailable;
    private boolean previewAvailable;
    private boolean orderAvailable;
    private String projectPhase;

    public static ProjectDetailResponse from(Project project) {
        return ProjectDetailResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .coverImageUrl(project.getCoverImageUrl())
                .status(project.getStatus())
                .influencerId(project.getInfluencer().getId())
                .influencerName(project.getInfluencer().getName())
                .influencerDisplayName(project.getInfluencer().getDisplayName())
                .influencerProfileImageUrl(project.getInfluencer().getProfileImageUrl())
                .createdByUserId(project.getCreatedBy().getId())
                .createdByNickname(project.getCreatedBy().getNickname())
                .uploadStartAt(project.getUploadStartAt())
                .uploadEndAt(project.getUploadEndAt())
                .previewStartAt(project.getPreviewStartAt())
                .previewEndAt(project.getPreviewEndAt())
                .orderStartAt(project.getOrderStartAt())
                .orderEndAt(project.getOrderEndAt())
                .build();
    }
}