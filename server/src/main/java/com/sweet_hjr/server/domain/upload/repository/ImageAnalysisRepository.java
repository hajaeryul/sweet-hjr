package com.sweet_hjr.server.domain.upload.repository;

import com.sweet_hjr.server.domain.upload.entity.ImageAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageAnalysisRepository extends JpaRepository<ImageAnalysis, Long> {
    Optional<ImageAnalysis> findByFanUploadFileId(Long fanUploadFileId);
}