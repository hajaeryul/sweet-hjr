package com.sweet_hjr.server.domain.photobook.repository;

import com.sweet_hjr.server.domain.photobook.entity.Photobook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotobookRepository extends JpaRepository<Photobook, Long> {
    Optional<Photobook> findByProjectId(Long projectId);
    boolean existsByProjectId(Long projectId);
}