package com.sweet_hjr.server.domain.photobook.entity;

import com.sweet_hjr.server.domain.upload.entity.CuratedImage;
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
        name = "photobook_pages",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_photobook_page_no", columnNames = {"photobook_id", "page_number"})
        }
)
public class PhotobookPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "photobook_id")
    private Photobook photobook;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curated_image_id")
    private CuratedImage curatedImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "page_role", nullable = false, length = 20)
    private PhotobookPageRole pageRole;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;

    @Column(name = "template_uid", length = 100)
    private String templateUid;

    @Column(name = "image_source_url", nullable = false, length = 500)
    private String imageSourceUrl;

    @Column(name = "sweetbook_file_name", length = 255)
    private String sweetbookFileName;

    @Column(name = "caption", length = 300)
    private String caption;

    @Column(name = "priority_order", nullable = false)
    private Integer priorityOrder;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public PhotobookPage(
            Photobook photobook,
            CuratedImage curatedImage,
            PhotobookPageRole pageRole,
            Integer pageNumber,
            String templateUid,
            String imageSourceUrl,
            String sweetbookFileName,
            String caption,
            Integer priorityOrder,
            LocalDateTime createdAt
    ) {
        this.photobook = photobook;
        this.curatedImage = curatedImage;
        this.pageRole = pageRole;
        this.pageNumber = pageNumber;
        this.templateUid = templateUid;
        this.imageSourceUrl = imageSourceUrl;
        this.sweetbookFileName = sweetbookFileName;
        this.caption = caption;
        this.priorityOrder = priorityOrder;
        this.createdAt = createdAt;
    }

    public void assignTemplateUid(String templateUid) {
        this.templateUid = templateUid;
    }

    public void assignSweetbookFileName(String sweetbookFileName) {
        this.sweetbookFileName = sweetbookFileName;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}