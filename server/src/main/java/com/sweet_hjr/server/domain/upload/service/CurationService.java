package com.sweet_hjr.server.domain.upload.service;

import com.sweet_hjr.server.domain.project.entity.Project;
import com.sweet_hjr.server.domain.project.repository.ProjectRepository;
import com.sweet_hjr.server.domain.upload.dto.*;
import com.sweet_hjr.server.domain.upload.entity.*;
import com.sweet_hjr.server.domain.upload.repository.*;
import com.sweet_hjr.server.domain.user.entity.User;
import com.sweet_hjr.server.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CurationService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ImageGroupRepository imageGroupRepository;
    private final ImageGroupItemRepository imageGroupItemRepository;
    private final CuratedImageRepository curatedImageRepository;
    private final FanUploadFileRepository fanUploadFileRepository;
    private final ImageAnalysisRepository imageAnalysisRepository;

    @Transactional(readOnly = true)
    public List<CurationGroupSummaryResponse> getProjectGroups(Long projectId) {
        return imageGroupRepository.findAllByProjectIdOrderByIdDesc(projectId)
                .stream()
                .map(group -> CurationGroupSummaryResponse.builder()
                        .imageGroupId(group.getId())
                        .groupKey(group.getGroupKey())
                        .representativeImageId(
                                group.getRepresentativeImage() != null ? group.getRepresentativeImage().getId() : null
                        )
                        .representativeImageUrl(
                                group.getRepresentativeImage() != null ? group.getRepresentativeImage().getFileUrl() : null
                        )
                        .itemCount(imageGroupItemRepository.findAllByImageGroupIdOrderByIdAsc(group.getId()).size())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CurationGroupItemResponse> getGroupItems(Long projectId, Long imageGroupId) {
        ImageGroup imageGroup = imageGroupRepository.findById(imageGroupId)
                .orElseThrow(() -> new EntityNotFoundException("이미지 그룹을 찾을 수 없습니다. id=" + imageGroupId));

        if (!imageGroup.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("프로젝트와 이미지 그룹 정보가 일치하지 않습니다.");
        }

        List<ImageGroupItem> items = imageGroupItemRepository.findAllByImageGroupIdOrderByIdAsc(imageGroupId);

        return items.stream()
                .map(item -> {
                    FanUploadFile file = item.getFanUploadFile();
                    ImageAnalysis analysis = imageAnalysisRepository.findByFanUploadFileId(file.getId()).orElse(null);

                    String aiReviewStatus = getAiReviewStatus(analysis);
                    String aiReviewSummary = getAiReviewSummary(analysis);

                    return CurationGroupItemResponse.builder()
                            .fileId(file.getId())
                            .userId(file.getUser().getId())
                            .userNickname(file.getUser().getNickname())
                            .originalFileName(file.getOriginalFileName())
                            .fileUrl(file.getFileUrl())
                            .width(file.getWidth())
                            .height(file.getHeight())
                            .fileSize(file.getFileSize())
                            .aiReviewStatus(aiReviewStatus)
                            .aiReviewSummary(aiReviewSummary)
                            .resolutionScore(analysis != null ? analysis.getResolutionScore() : null)
                            .brightnessScore(analysis != null ? analysis.getBrightnessScore() : null)
                            .blurScore(analysis != null ? analysis.getBlurScore() : null)
                            .noiseScore(analysis != null ? analysis.getNoiseScore() : null)
                            .watermarkDetected(analysis != null ? analysis.getWatermarkDetected() : null)
                            .textDetected(analysis != null ? analysis.getTextDetected() : null)
                            .inappropriateFlag(analysis != null ? analysis.getInappropriateFlag() : null)
                            .build();
                })
                .toList();
    }

    private String getAiReviewStatus(ImageAnalysis analysis) {
        if (analysis == null) {
            return "UNKNOWN";
        }

        if (Boolean.TRUE.equals(analysis.getInappropriateFlag())) {
            return "REJECT_SUGGESTED";
        }

        if (Boolean.TRUE.equals(analysis.getWatermarkDetected())
                || Boolean.TRUE.equals(analysis.getTextDetected())) {
            return "FLAGGED";
        }

        if (analysis.getResolutionScore() != null && analysis.getResolutionScore().doubleValue() < 0.5) {
            return "FLAGGED";
        }

        if (analysis.getBlurScore() != null && analysis.getBlurScore().doubleValue() > 0.4) {
            return "FLAGGED";
        }

        return "PASS";
    }

    private String getAiReviewSummary(ImageAnalysis analysis) {
        if (analysis == null) {
            return "분석 결과가 없습니다.";
        }

        if (Boolean.TRUE.equals(analysis.getInappropriateFlag())) {
            return "부적절 가능성이 있어 검토가 필요합니다.";
        }

        if (Boolean.TRUE.equals(analysis.getWatermarkDetected())) {
            return "워터마크가 감지되어 검토가 필요합니다.";
        }

        if (Boolean.TRUE.equals(analysis.getTextDetected())) {
            return "텍스트가 감지되어 검토가 필요합니다.";
        }

        if (analysis.getResolutionScore() != null && analysis.getResolutionScore().doubleValue() < 0.5) {
            return "해상도가 낮아 품질 검토가 필요합니다.";
        }

        if (analysis.getBlurScore() != null && analysis.getBlurScore().doubleValue() > 0.4) {
            return "흐림 정도가 높아 검토가 필요합니다.";
        }

        return "기본 품질 검토를 통과했습니다.";
    }

    public CurateImageResponse curateImage(Long projectId, CurateImageRequest request) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트를 찾을 수 없습니다. id=" + projectId));

        // ✅ 중복 체크
        boolean alreadySelected = curatedImageRepository
                .existsByProjectIdAndSourceImageId(projectId, request.getSourceImageId());

        if (alreadySelected) {
            throw new IllegalStateException("이미 선정된 이미지입니다.");
        }

        User selectedBy = userRepository.findById(request.getSelectedBy())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        ImageGroup imageGroup = imageGroupRepository.findById(request.getImageGroupId())
                .orElseThrow(() -> new EntityNotFoundException("이미지 그룹을 찾을 수 없습니다."));

        CuratedImage curatedImage = curatedImageRepository.save(
                CuratedImage.builder()
                        .project(project)
                        .sourceType("FAN_UPLOAD_FILE")
                        .sourceImageId(request.getSourceImageId())
                        .imageGroup(imageGroup)
                        .selectedBy(selectedBy)
                        .selectedReason(request.getSelectedReason())
                        .priorityOrder(request.getPriorityOrder())
                        .build()
        );

        return CurateImageResponse.builder()
                .curatedImageId(curatedImage.getId())
                .projectId(projectId)
                .sourceImageId(curatedImage.getSourceImageId())
                .imageGroupId(imageGroup.getId())
                .sourceType(curatedImage.getSourceType())
                .priorityOrder(curatedImage.getPriorityOrder())
                .build();
    }

    @Transactional(readOnly = true)
    public List<CuratedImagePreviewResponse> getCuratedImages(Long projectId) {
        return curatedImageRepository.findAllByProjectIdOrderByPriorityOrderAscCreatedAtAsc(projectId)
                .stream()
                .map(curatedImage -> CuratedImagePreviewResponse.builder()
                        .curatedImageId(curatedImage.getId())
                        .sourceImageId(curatedImage.getSourceImageId())
                        .fileUrl(resolveSourceImageUrl(curatedImage))
                        .priorityOrder(curatedImage.getPriorityOrder())
                        .selectedReason(curatedImage.getSelectedReason())
                        .build())
                .toList();
    }

    private String resolveSourceImageUrl(CuratedImage curatedImage) {
        if (!"FAN_UPLOAD_FILE".equals(curatedImage.getSourceType())) {
            return null;
        }

        return fanUploadFileRepository.findById(curatedImage.getSourceImageId())
                .map(FanUploadFile::getFileUrl)
                .orElse(null);
    }
}