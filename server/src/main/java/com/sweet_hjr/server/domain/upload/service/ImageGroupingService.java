package com.sweet_hjr.server.domain.upload.service;

import com.sweet_hjr.server.domain.project.entity.Project;
import com.sweet_hjr.server.domain.project.repository.ProjectRepository;
import com.sweet_hjr.server.domain.upload.entity.FanUploadFile;
import com.sweet_hjr.server.domain.upload.entity.ImageAnalysis;
import com.sweet_hjr.server.domain.upload.entity.ImageGroup;
import com.sweet_hjr.server.domain.upload.entity.ImageGroupItem;
import com.sweet_hjr.server.domain.upload.repository.FanUploadFileRepository;
import com.sweet_hjr.server.domain.upload.repository.ImageAnalysisRepository;
import com.sweet_hjr.server.domain.upload.repository.ImageGroupItemRepository;
import com.sweet_hjr.server.domain.upload.repository.ImageGroupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageGroupingService {

    private final ProjectRepository projectRepository;
    private final FanUploadFileRepository fanUploadFileRepository;
    private final ImageAnalysisRepository imageAnalysisRepository;
    private final ImageGroupRepository imageGroupRepository;
    private final ImageGroupItemRepository imageGroupItemRepository;

    public void regroupProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트를 찾을 수 없습니다. id=" + projectId));

        List<FanUploadFile> files = fanUploadFileRepository.findAllByProjectIdOrderByUploadedAtDesc(projectId);
        if (files.isEmpty()) {
            return;
        }

        Map<String, List<FanUploadFile>> groupedFiles = files.stream()
                .collect(Collectors.groupingBy(this::buildSimpleGroupKey));

        for (Map.Entry<String, List<FanUploadFile>> entry : groupedFiles.entrySet()) {
            String groupKey = entry.getKey();
            List<FanUploadFile> groupFiles = entry.getValue();

            ImageGroup imageGroup = imageGroupRepository.findByProjectIdAndGroupKey(projectId, groupKey)
                    .orElseGet(() -> imageGroupRepository.save(
                            ImageGroup.builder()
                                    .project(project)
                                    .groupKey(groupKey)
                                    .representativeImage(null)
                                    .clusterConfidence(BigDecimal.valueOf(0.8000))
                                    .createdAt(LocalDateTime.now())
                                    .build()
                    ));

            FanUploadFile representative = chooseRepresentative(groupFiles);
            imageGroup.changeRepresentativeImage(representative);
            imageGroup.changeClusterConfidence(BigDecimal.valueOf(0.8000));

            for (FanUploadFile file : groupFiles) {
                boolean exists = imageGroupItemRepository.existsByImageGroupIdAndFanUploadFileId(
                        imageGroup.getId(),
                        file.getId()
                );

                if (!exists) {
                    imageGroupItemRepository.save(
                            ImageGroupItem.builder()
                                    .imageGroup(imageGroup)
                                    .fanUploadFile(file)
                                    .similarityDistance(BigDecimal.ZERO)
                                    .createdAt(LocalDateTime.now())
                                    .build()
                    );
                }
            }
        }
    }

    private String buildSimpleGroupKey(FanUploadFile file) {
        String fileName = Optional.ofNullable(file.getOriginalFileName())
                .orElse("unknown")
                .toLowerCase(Locale.ROOT);

        String ext = fileName.contains(".")
                ? fileName.substring(fileName.lastIndexOf('.') + 1)
                : "unknown";

        String resolutionBucket = buildResolutionBucket(file.getWidth(), file.getHeight());

        return resolutionBucket + ":" + ext;
    }

    private String buildResolutionBucket(Integer width, Integer height) {
        if (width == null || height == null) {
            return "unknown";
        }

        long pixels = (long) width * height;
        if (pixels >= 2_000_000L) {
            return "high";
        }
        if (pixels >= 1_000_000L) {
            return "mid";
        }
        return "low";
    }

    private FanUploadFile chooseRepresentative(List<FanUploadFile> groupFiles) {
        return groupFiles.stream()
                .max(Comparator.comparing(this::representativeScore))
                .orElseThrow();
    }

    private BigDecimal representativeScore(FanUploadFile file) {
        return imageAnalysisRepository.findByFanUploadFileId(file.getId())
                .map(this::calculateRepresentativeScore)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateRepresentativeScore(ImageAnalysis analysis) {
        return analysis.getResolutionScore()
                .add(analysis.getBrightnessScore())
                .subtract(analysis.getBlurScore());
    }
}