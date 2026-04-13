package com.sweet_hjr.server.domain.upload.repository;

import com.sweet_hjr.server.domain.upload.entity.FanUpload;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FanUploadRepository extends JpaRepository<FanUpload, Long> {

    Optional<FanUpload> findByIdAndProjectIdAndUserId(Long id, Long projectId, Long userId);

    @EntityGraph(attributePaths = {"project", "user"})
    List<FanUpload> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}