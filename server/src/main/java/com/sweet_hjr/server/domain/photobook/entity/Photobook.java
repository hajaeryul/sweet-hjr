package com.sweet_hjr.server.domain.photobook.entity;

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
@Table(
        name = "photobooks",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_photobook_project", columnNames = {"project_id"})
        }
)
public class Photobook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "book_spec_uid", nullable = false, length = 100)
    private String bookSpecUid;

    @Column(name = "spec_profile_uid", length = 100)
    private String specProfileUid;

    @Column(name = "external_ref", nullable = false, length = 100)
    private String externalRef;

    @Column(name = "sweetbook_book_uid", length = 100)
    private String sweetbookBookUid;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PhotobookStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "cover_curated_image_id")
    private Long coverCuratedImageId;

    @Column(name = "final_page_count")
    private Integer finalPageCount;

    @Column(name = "last_error_message", length = 1000)
    private String lastErrorMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Photobook(
            Project project,
            String title,
            String bookSpecUid,
            String specProfileUid,
            String externalRef,
            String sweetbookBookUid,
            PhotobookStatus status,
            User createdBy,
            Long coverCuratedImageId,
            Integer finalPageCount,
            String lastErrorMessage,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.project = project;
        this.title = title;
        this.bookSpecUid = bookSpecUid;
        this.specProfileUid = specProfileUid;
        this.externalRef = externalRef;
        this.sweetbookBookUid = sweetbookBookUid;
        this.status = status;
        this.createdBy = createdBy;
        this.coverCuratedImageId = coverCuratedImageId;
        this.finalPageCount = finalPageCount;
        this.lastErrorMessage = lastErrorMessage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void markBookCreated(String sweetbookBookUid) {
        this.sweetbookBookUid = sweetbookBookUid;
        this.status = PhotobookStatus.BOOK_CREATED;
        this.lastErrorMessage = null;
    }

    public void markCoverAdded() {
        this.status = PhotobookStatus.COVER_ADDED;
        this.lastErrorMessage = null;
    }

    public void markContentsAdded() {
        this.status = PhotobookStatus.CONTENTS_ADDED;
        this.lastErrorMessage = null;
    }

    public void markFinalized(Integer finalPageCount) {
        this.status = PhotobookStatus.FINALIZED;
        this.finalPageCount = finalPageCount;
        this.lastErrorMessage = null;
    }

    public void markFailed(String lastErrorMessage) {
        this.status = PhotobookStatus.FAILED;
        this.lastErrorMessage = lastErrorMessage;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}