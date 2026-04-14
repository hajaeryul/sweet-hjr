package com.sweet_hjr.server.domain.upload.entity;

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
        name = "image_group_items",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_image_group_items_group_file", columnNames = {"image_group_id", "fan_upload_file_id"})
        }
)
public class ImageGroupItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "image_group_id")
    private ImageGroup imageGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fan_upload_file_id")
    private FanUploadFile fanUploadFile;

    @Column(name = "similarity_distance", precision = 10, scale = 4)
    private BigDecimal similarityDistance;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public ImageGroupItem(
            ImageGroup imageGroup,
            FanUploadFile fanUploadFile,
            BigDecimal similarityDistance,
            LocalDateTime createdAt
    ) {
        this.imageGroup = imageGroup;
        this.fanUploadFile = fanUploadFile;
        this.similarityDistance = similarityDistance;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}