package com.sweet_hjr.server.domain.upload.entity;

import com.sweet_hjr.server.domain.project.entity.Project;
import com.sweet_hjr.server.domain.user.entity.User;
import com.sweet_hjr.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "fan_uploads")
public class FanUpload extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UploadStatus status;

    @Column(length = 500)
    private String memo;

    @Builder
    public FanUpload(Project project, User user, UploadStatus status, String memo) {
        this.project = project;
        this.user = user;
        this.status = status;
        this.memo = memo;
    }
}