package com.sweet_hjr.server.domain.project.dto;

import com.sweet_hjr.server.domain.project.entity.ProjectStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProjectSummaryResponse {

    private Long id;
    private String title;
    private String description;
    private String coverImageUrl;
    private ProjectStatus status;

    private Long influencerId;
    private String influencerName;
    private String influencerDisplayName;

    private LocalDateTime uploadStartAt;
    private LocalDateTime uploadEndAt;

    private boolean uploadAvailable;
    private String projectPhase;
}