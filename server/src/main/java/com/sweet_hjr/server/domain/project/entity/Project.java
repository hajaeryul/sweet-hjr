package com.sweet_hjr.server.domain.project.entity;

import com.sweet_hjr.server.domain.influencer.entity.Influencer;
import com.sweet_hjr.server.domain.user.entity.User;
import com.sweet_hjr.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "projects")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "influencer_id")
    private Influencer influencer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProjectStatus status;

    @Column(name = "upload_start_at")
    private LocalDateTime uploadStartAt;

    @Column(name = "upload_end_at")
    private LocalDateTime uploadEndAt;

    @Column(name = "preview_start_at")
    private LocalDateTime previewStartAt;

    @Column(name = "preview_end_at")
    private LocalDateTime previewEndAt;

    @Column(name = "order_start_at")
    private LocalDateTime orderStartAt;

    @Column(name = "order_end_at")
    private LocalDateTime orderEndAt;

    @Builder
    public Project(
            Influencer influencer,
            User createdBy,
            String title,
            String description,
            String coverImageUrl,
            ProjectStatus status,
            LocalDateTime uploadStartAt,
            LocalDateTime uploadEndAt,
            LocalDateTime previewStartAt,
            LocalDateTime previewEndAt,
            LocalDateTime orderStartAt,
            LocalDateTime orderEndAt
    ) {
        this.influencer = influencer;
        this.createdBy = createdBy;
        this.title = title;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.status = status;
        this.uploadStartAt = uploadStartAt;
        this.uploadEndAt = uploadEndAt;
        this.previewStartAt = previewStartAt;
        this.previewEndAt = previewEndAt;
        this.orderStartAt = orderStartAt;
        this.orderEndAt = orderEndAt;
    }
}