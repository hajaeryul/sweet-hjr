package com.sweet_hjr.server.domain.upload.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    StoredFileInfo store(Long projectId, Long fanUploadId, MultipartFile file) throws IOException;
}