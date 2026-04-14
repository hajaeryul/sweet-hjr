package com.sweet_hjr.server.domain.upload.repository;

import com.sweet_hjr.server.domain.upload.entity.FanUploadFile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FanUploadFileRepository extends JpaRepository<FanUploadFile, Long> {

    long countByFanUploadId(Long fanUploadId);

    @EntityGraph(attributePaths = {"fanUpload", "user"})
    List<FanUploadFile> findAllByProjectIdOrderByUploadedAtDesc(Long projectId);

    @EntityGraph(attributePaths = {"fanUpload", "user"})
    List<FanUploadFile> findAllByFanUploadIdInOrderByUploadedAtDesc(List<Long> fanUploadIds);

    @EntityGraph(attributePaths = {"fanUpload", "user", "project"})
    List<FanUploadFile> findAllByFanUploadId(Long fanUploadId);
}