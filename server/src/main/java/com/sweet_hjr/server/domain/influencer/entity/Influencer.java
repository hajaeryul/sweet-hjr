package com.sweet_hjr.server.domain.influencer.entity;

import com.sweet_hjr.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "influencers")
public class Influencer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InfluencerCategory category;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InfluencerStatus status;

    @Builder
    public Influencer(
            String name,
            String displayName,
            InfluencerCategory category,
            String profileImageUrl,
            InfluencerStatus status
    ) {
        this.name = name;
        this.displayName = displayName;
        this.category = category;
        this.profileImageUrl = profileImageUrl;
        this.status = status;
    }
}