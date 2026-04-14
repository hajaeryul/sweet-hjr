package com.sweet_hjr.server.domain.upload.repository;

import com.sweet_hjr.server.domain.upload.entity.ImageGroupItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageGroupItemRepository extends JpaRepository<ImageGroupItem, Long> {

    boolean existsByImageGroupIdAndFanUploadFileId(Long imageGroupId, Long fanUploadFileId);

    @EntityGraph(attributePaths = {"fanUploadFile", "fanUploadFile.user"})
    List<ImageGroupItem> findAllByImageGroupIdOrderByIdAsc(Long imageGroupId);
}