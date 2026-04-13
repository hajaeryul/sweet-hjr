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
@Table(name = "fan_upload_files")
public class FanUploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fan_upload_id")
    private FanUpload fanUpload;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "original_file_name", nullable = false, length = 255)
    private String originalFileName;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private Integer width;

    @Column(nullable = false)
    private Integer height;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Builder
    public FanUploadFile(
            FanUpload fanUpload,
            Project project,
            User user,
            String originalFileName,
            String fileUrl,
            String thumbnailUrl,
            String mimeType,
            Long fileSize,
            Integer width,
            Integer height,
            LocalDateTime uploadedAt
    ) {
        this.fanUpload = fanUpload;
        this.project = project;
        this.user = user;
        this.originalFileName = originalFileName;
        this.fileUrl = fileUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.width = width;
        this.height = height;
        this.uploadedAt = uploadedAt;
    }

    @PrePersist
    public void prePersist() {
        if (uploadedAt == null) {
            uploadedAt = LocalDateTime.now();
        }
    }
}