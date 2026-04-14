package com.sweet_hjr.server.domain.photobook.repository;

import com.sweet_hjr.server.domain.photobook.entity.PhotobookSyncLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotobookSyncLogRepository extends JpaRepository<PhotobookSyncLog, Long> {
}