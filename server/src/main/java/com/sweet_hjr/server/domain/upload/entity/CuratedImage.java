package com.sweet_hjr.server.domain.upload.entity;

import com.sweet_hjr.server.domain.project.entity.Project;
import com.sweet_hjr.server.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "curated_images")
public class CuratedImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "source_type", nullable = false, length = 30)
    private String sourceType;

    @Column(name = "source_image_id", nullable = false)
    private Long sourceImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_group_id")
    private ImageGroup imageGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "selected_by")
    private User selectedBy;

    @Column(name = "selected_reason", length = 300)
    private String selectedReason;

    @Column(name = "priority_order", nullable = false)
    private Integer priorityOrder;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public CuratedImage(
            Project project,
            String sourceType,
            Long sourceImageId,
            ImageGroup imageGroup,
            User selectedBy,
            String selectedReason,
            Integer priorityOrder,
            LocalDateTime createdAt
    ) {
        this.project = project;
        this.sourceType = sourceType;
        this.sourceImageId = sourceImageId;
        this.imageGroup = imageGroup;
        this.selectedBy = selectedBy;
        this.selectedReason = selectedReason;
        this.priorityOrder = priorityOrder;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}