package com.sweet_hjr.server.domain.upload.repository;

import com.sweet_hjr.server.domain.upload.entity.ImageGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageGroupRepository extends JpaRepository<ImageGroup, Long> {

    Optional<ImageGroup> findByProjectIdAndGroupKey(Long projectId, String groupKey);

    @EntityGraph(attributePaths = {"representativeImage"})
    List<ImageGroup> findAllByProjectIdOrderByIdDesc(Long projectId);
}