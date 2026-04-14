package com.sweet_hjr.server.domain.photobook.repository;

import com.sweet_hjr.server.domain.photobook.entity.PhotobookPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotobookPageRepository extends JpaRepository<PhotobookPage, Long> {
    List<PhotobookPage> findAllByPhotobookIdOrderByPageNumberAsc(Long photobookId);
    void deleteAllByPhotobookId(Long photobookId);
}