package com.sweet_hjr.server.domain.photobook.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "photobook_sync_logs")
public class PhotobookSyncLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photobook_id", nullable = false)
    private Long photobookId;

    @Column(name = "step_name", nullable = false, length = 50)
    private String stepName;

    @Column(name = "success", nullable = false)
    private Boolean success;

    @Column(name = "request_summary", length = 1000)
    private String requestSummary;

    @Column(name = "response_summary", length = 1000)
    private String responseSummary;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public PhotobookSyncLog(
            Long photobookId,
            String stepName,
            Boolean success,
            String requestSummary,
            String responseSummary,
            String errorMessage,
            LocalDateTime createdAt
    ) {
        this.photobookId = photobookId;
        this.stepName = stepName;
        this.success = success;
        this.requestSummary = requestSummary;
        this.responseSummary = responseSummary;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}