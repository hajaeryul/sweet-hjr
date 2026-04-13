package com.sweet_hjr.server.domain.project.service;

import com.sweet_hjr.server.domain.influencer.entity.Influencer;
import com.sweet_hjr.server.domain.influencer.repository.InfluencerRepository;
import com.sweet_hjr.server.domain.project.dto.ProjectCreateRequest;
import com.sweet_hjr.server.domain.project.dto.ProjectDetailResponse;
import com.sweet_hjr.server.domain.project.dto.ProjectResponse;
import com.sweet_hjr.server.domain.project.dto.ProjectSummaryResponse;
import com.sweet_hjr.server.domain.project.entity.Project;
import com.sweet_hjr.server.domain.project.entity.ProjectStatus;
import com.sweet_hjr.server.domain.project.repository.ProjectRepository;
import com.sweet_hjr.server.domain.user.entity.User;
import com.sweet_hjr.server.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import com.sweet_hjr.server.domain.project.entity.ProjectPhase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final InfluencerRepository influencerRepository;
    private final UserRepository userRepository;

    public ProjectResponse createProject(ProjectCreateRequest request) {
        Influencer influencer = influencerRepository.findById(request.getInfluencerId())
                .orElseThrow(() -> new EntityNotFoundException("인플루언서를 찾을 수 없습니다."));

        User createdBy = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new EntityNotFoundException("생성자를 찾을 수 없습니다."));

        Project project = Project.builder()
                .influencer(influencer)
                .createdBy(createdBy)
                .title(request.getTitle())
                .description(request.getDescription())
                .coverImageUrl(request.getCoverImageUrl())
                .status(ProjectStatus.DRAFT)
                .uploadStartAt(request.getUploadStartAt())
                .uploadEndAt(request.getUploadEndAt())
                .previewStartAt(request.getPreviewStartAt())
                .previewEndAt(request.getPreviewEndAt())
                .orderStartAt(request.getOrderStartAt())
                .orderEndAt(request.getOrderEndAt())
                .build();

        Project savedProject = projectRepository.save(project);
        return ProjectResponse.from(savedProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectSummaryResponse> getProjects() {
        return projectRepository.findAllByOrderByIdDesc()
                .stream()
                .map(project -> ProjectSummaryResponse.builder()
                        .id(project.getId())
                        .title(project.getTitle())
                        .description(project.getDescription())
                        .coverImageUrl(project.getCoverImageUrl())
                        .status(project.getStatus())
                        .influencerId(project.getInfluencer().getId())
                        .influencerName(project.getInfluencer().getName())
                        .influencerDisplayName(project.getInfluencer().getDisplayName())
                        .uploadStartAt(project.getUploadStartAt())
                        .uploadEndAt(project.getUploadEndAt())
                        .uploadAvailable(isNowBetween(project.getUploadStartAt(), project.getUploadEndAt()))
                        .projectPhase(calculateProjectPhase(project).name())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectDetailResponse getProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트를 찾을 수 없습니다. id=" + projectId));

        ProjectDetailResponse response = ProjectDetailResponse.from(project);

        return ProjectDetailResponse.builder()
                .id(response.getId())
                .title(response.getTitle())
                .description(response.getDescription())
                .coverImageUrl(response.getCoverImageUrl())
                .status(response.getStatus())
                .influencerId(response.getInfluencerId())
                .influencerName(response.getInfluencerName())
                .influencerDisplayName(response.getInfluencerDisplayName())
                .influencerProfileImageUrl(response.getInfluencerProfileImageUrl())
                .createdByUserId(response.getCreatedByUserId())
                .createdByNickname(response.getCreatedByNickname())
                .uploadStartAt(response.getUploadStartAt())
                .uploadEndAt(response.getUploadEndAt())
                .previewStartAt(response.getPreviewStartAt())
                .previewEndAt(response.getPreviewEndAt())
                .orderStartAt(response.getOrderStartAt())
                .orderEndAt(response.getOrderEndAt())
                .uploadAvailable(isNowBetween(project.getUploadStartAt(), project.getUploadEndAt()))
                .previewAvailable(isNowBetween(project.getPreviewStartAt(), project.getPreviewEndAt()))
                .orderAvailable(isNowBetween(project.getOrderStartAt(), project.getOrderEndAt()))
                .projectPhase(calculateProjectPhase(project).name())
                .build();
    }

    private boolean isNowBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(start) && !now.isAfter(end);
    }

    private ProjectPhase calculateProjectPhase(Project project) {
        LocalDateTime now = LocalDateTime.now();

        if (project.getUploadStartAt() != null && now.isBefore(project.getUploadStartAt())) {
            return ProjectPhase.BEFORE_UPLOAD;
        }

        if (isBetween(now, project.getUploadStartAt(), project.getUploadEndAt())) {
            return ProjectPhase.UPLOAD_OPEN;
        }

        if (isBetween(now, project.getPreviewStartAt(), project.getPreviewEndAt())) {
            return ProjectPhase.PREVIEW_OPEN;
        }

        if (isBetween(now, project.getOrderStartAt(), project.getOrderEndAt())) {
            return ProjectPhase.ORDER_OPEN;
        }

        return ProjectPhase.CLOSED;
    }

    private boolean isBetween(LocalDateTime now, LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return false;
        }
        return !now.isBefore(start) && !now.isAfter(end);
    }
}