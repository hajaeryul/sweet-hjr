package com.sweet_hjr.server.domain.project.entity;

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
@Table(name = "project_official_images")
public class ProjectOfficialImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 30)
    private OfficialImageSourceType sourceType;

    @Column(name = "taken_at")
    private LocalDateTime takenAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @Builder
    public ProjectOfficialImage(
            Project project,
            String fileUrl,
            String fileName,
            OfficialImageSourceType sourceType,
            LocalDateTime takenAt,
            User uploadedBy
    ) {
        this.project = project;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.sourceType = sourceType;
        this.takenAt = takenAt;
        this.uploadedBy = uploadedBy;
    }
}