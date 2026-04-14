package com.sweet_hjr.server.domain.upload.repository;

import com.sweet_hjr.server.domain.upload.entity.CuratedImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuratedImageRepository extends JpaRepository<CuratedImage, Long> {
    List<CuratedImage> findAllByProjectIdOrderByPriorityOrderAscCreatedAtAsc(Long projectId);
    boolean existsByProjectIdAndSourceImageId(Long projectId, Long sourceImageId);
}