package com.sweet_hjr.server.domain.upload.service;

import com.sweet_hjr.server.domain.upload.entity.FanUploadFile;
import com.sweet_hjr.server.domain.upload.entity.ImageAnalysis;
import com.sweet_hjr.server.domain.upload.repository.ImageAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageAnalysisService {

    private final ImageAnalysisRepository imageAnalysisRepository;

    public void analyze(FanUploadFile file) {
        BigDecimal resolutionScore = calculateResolutionScore(file.getWidth(), file.getHeight());
        BigDecimal brightnessScore = calculateBrightnessScore(file.getWidth(), file.getHeight());
        BigDecimal blurScore = calculateBlurScore(file.getWidth(), file.getHeight());
        BigDecimal noiseScore = BigDecimal.valueOf(0.1000);

        boolean faceDetected = false;
        int faceCount = 0;
        boolean watermarkDetected = false;
        boolean textDetected = false;
        boolean inappropriateFlag = false;

        Optional<ImageAnalysis> optionalAnalysis =
                imageAnalysisRepository.findByFanUploadFileId(file.getId());

        if (optionalAnalysis.isPresent()) {
            optionalAnalysis.get().updateAnalysis(
                    blurScore,
                    brightnessScore,
                    noiseScore,
                    faceDetected,
                    faceCount,
                    resolutionScore,
                    watermarkDetected,
                    textDetected,
                    inappropriateFlag,
                    null
            );
            return;
        }

        ImageAnalysis analysis = ImageAnalysis.builder()
                .fanUploadFile(file)
                .blurScore(blurScore)
                .brightnessScore(brightnessScore)
                .noiseScore(noiseScore)
                .faceDetected(faceDetected)
                .faceCount(faceCount)
                .resolutionScore(resolutionScore)
                .watermarkDetected(watermarkDetected)
                .textDetected(textDetected)
                .inappropriateFlag(inappropriateFlag)
                .similarityVectorPath(null)
                .analyzedAt(LocalDateTime.now())
                .build();

        imageAnalysisRepository.save(analysis);
    }

    private BigDecimal calculateResolutionScore(Integer width, Integer height) {
        if (width == null || height == null || width <= 0 || height <= 0) {
            return BigDecimal.valueOf(0.0000);
        }

        long pixels = (long) width * height;

        if (pixels >= 2_000_000L) {
            return BigDecimal.valueOf(1.0000);
        }
        if (pixels >= 1_000_000L) {
            return BigDecimal.valueOf(0.8000);
        }
        if (pixels >= 500_000L) {
            return BigDecimal.valueOf(0.5000);
        }
        return BigDecimal.valueOf(0.2000);
    }

    private BigDecimal calculateBrightnessScore(Integer width, Integer height) {
        if (width == null || height == null) {
            return BigDecimal.valueOf(0.5000);
        }

        if (width >= 1200 && height >= 1200) {
            return BigDecimal.valueOf(0.8500);
        }
        return BigDecimal.valueOf(0.7000);
    }

    private BigDecimal calculateBlurScore(Integer width, Integer height) {
        if (width == null || height == null) {
            return BigDecimal.valueOf(0.3000);
        }

        if (width >= 1200 && height >= 1200) {
            return BigDecimal.valueOf(0.1500);
        }
        return BigDecimal.valueOf(0.2500);
    }
}