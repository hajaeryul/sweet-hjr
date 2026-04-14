package com.sweet_hjr.server.domain.upload.entity;

import com.sweet_hjr.server.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "image_groups",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_image_groups_project_group_key", columnNames = {"project_id", "group_key"})
        }
)
public class ImageGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "group_key", nullable = false, length = 100)
    private String groupKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "representative_image_id")
    private FanUploadFile representativeImage;

    @Column(name = "cluster_confidence", precision = 10, scale = 4)
    private BigDecimal clusterConfidence;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public ImageGroup(
            Project project,
            String groupKey,
            FanUploadFile representativeImage,
            BigDecimal clusterConfidence,
            LocalDateTime createdAt
    ) {
        this.project = project;
        this.groupKey = groupKey;
        this.representativeImage = representativeImage;
        this.clusterConfidence = clusterConfidence;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void changeRepresentativeImage(FanUploadFile representativeImage) {
        this.representativeImage = representativeImage;
    }

    public void changeClusterConfidence(BigDecimal clusterConfidence) {
        this.clusterConfidence = clusterConfidence;
    }
}