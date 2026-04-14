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
@Table(name = "image_analysis")
public class ImageAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fan_upload_file_id", nullable = false, unique = true)
    private FanUploadFile fanUploadFile;

    @Column(precision = 10, scale = 4)
    private BigDecimal blurScore;

    @Column(precision = 10, scale = 4)
    private BigDecimal brightnessScore;

    @Column(precision = 10, scale = 4)
    private BigDecimal noiseScore;

    @Column(nullable = false)
    private Boolean faceDetected;

    @Column(nullable = false)
    private Integer faceCount;

    @Column(precision = 10, scale = 4)
    private BigDecimal resolutionScore;

    @Column(nullable = false)
    private Boolean watermarkDetected;

    @Column(nullable = false)
    private Boolean textDetected;

    @Column(nullable = false)
    private Boolean inappropriateFlag;

    @Column(length = 500)
    private String similarityVectorPath;

    @Column(nullable = false)
    private LocalDateTime analyzedAt;

    @Builder
    public ImageAnalysis(
            FanUploadFile fanUploadFile,
            BigDecimal blurScore,
            BigDecimal brightnessScore,
            BigDecimal noiseScore,
            Boolean faceDetected,
            Integer faceCount,
            BigDecimal resolutionScore,
            Boolean watermarkDetected,
            Boolean textDetected,
            Boolean inappropriateFlag,
            String similarityVectorPath,
            LocalDateTime analyzedAt
    ) {
        this.fanUploadFile = fanUploadFile;
        this.blurScore = blurScore;
        this.brightnessScore = brightnessScore;
        this.noiseScore = noiseScore;
        this.faceDetected = faceDetected;
        this.faceCount = faceCount;
        this.resolutionScore = resolutionScore;
        this.watermarkDetected = watermarkDetected;
        this.textDetected = textDetected;
        this.inappropriateFlag = inappropriateFlag;
        this.similarityVectorPath = similarityVectorPath;
        this.analyzedAt = analyzedAt;
    }

    public void updateAnalysis(
            BigDecimal blurScore,
            BigDecimal brightnessScore,
            BigDecimal noiseScore,
            Boolean faceDetected,
            Integer faceCount,
            BigDecimal resolutionScore,
            Boolean watermarkDetected,
            Boolean textDetected,
            Boolean inappropriateFlag,
            String similarityVectorPath
    ) {
        this.blurScore = blurScore;
        this.brightnessScore = brightnessScore;
        this.noiseScore = noiseScore;
        this.faceDetected = faceDetected;
        this.faceCount = faceCount;
        this.resolutionScore = resolutionScore;
        this.watermarkDetected = watermarkDetected;
        this.textDetected = textDetected;
        this.inappropriateFlag = inappropriateFlag;
        this.similarityVectorPath = similarityVectorPath;
        this.analyzedAt = LocalDateTime.now();
    }
}