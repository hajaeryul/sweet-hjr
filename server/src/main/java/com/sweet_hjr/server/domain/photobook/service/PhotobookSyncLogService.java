package com.sweet_hjr.server.domain.photobook.service;

import com.sweet_hjr.server.domain.photobook.entity.PhotobookSyncLog;
import com.sweet_hjr.server.domain.photobook.repository.PhotobookSyncLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhotobookSyncLogService {

    private final PhotobookSyncLogRepository photobookSyncLogRepository;

    public void success(Long photobookId, String stepName, String requestSummary, String responseSummary) {
        photobookSyncLogRepository.save(
                PhotobookSyncLog.builder()
                        .photobookId(photobookId)
                        .stepName(stepName)
                        .success(true)
                        .requestSummary(requestSummary)
                        .responseSummary(responseSummary)
                        .build()
        );
    }

    public void fail(Long photobookId, String stepName, String requestSummary, Exception e) {
        photobookSyncLogRepository.save(
                PhotobookSyncLog.builder()
                        .photobookId(photobookId)
                        .stepName(stepName)
                        .success(false)
                        .requestSummary(requestSummary)
                        .errorMessage(e.getMessage())
                        .build()
        );
    }
}