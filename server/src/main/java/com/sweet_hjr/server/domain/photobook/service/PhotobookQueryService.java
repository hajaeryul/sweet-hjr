package com.sweet_hjr.server.domain.photobook.service;

import com.sweet_hjr.server.domain.photobook.dto.PhotobookDetailResponse;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookLayoutPreviewResponse;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookPagePreviewResponse;
import com.sweet_hjr.server.domain.photobook.entity.Photobook;
import com.sweet_hjr.server.domain.photobook.entity.PhotobookPage;
import com.sweet_hjr.server.domain.photobook.repository.PhotobookPageRepository;
import com.sweet_hjr.server.domain.photobook.repository.PhotobookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotobookQueryService {

    private final PhotobookLayoutService photobookLayoutService;
    private final PhotobookRepository photobookRepository;
    private final PhotobookPageRepository photobookPageRepository;

    public PhotobookLayoutPreviewResponse previewLayout(Long projectId, String bookSpecUid) {
        PhotobookLayoutModels.LayoutResult layout = photobookLayoutService.buildLayout(projectId, bookSpecUid);

        return PhotobookLayoutPreviewResponse.builder()
                .projectId(projectId)
                .contentPageCount(layout.getContents().size())
                .expectedFinalPageCount(layout.getExpectedFinalPageCount())
                .cover(
                        PhotobookPagePreviewResponse.builder()
                                .curatedImageId(layout.getCover().getCuratedImageId())
                                .pageNumber(layout.getCover().getPageNumber())
                                .pageRole(layout.getCover().getPageRole())
                                .imageUrl(layout.getCover().getImageUrl())
                                .caption(layout.getCover().getCaption())
                                .priorityOrder(layout.getCover().getPriorityOrder())
                                .build()
                )
                .pages(
                        layout.getContents().stream()
                                .map(page -> PhotobookPagePreviewResponse.builder()
                                        .curatedImageId(page.getCuratedImageId())
                                        .pageNumber(page.getPageNumber())
                                        .pageRole(page.getPageRole())
                                        .imageUrl(page.getImageUrl())
                                        .caption(page.getCaption())
                                        .priorityOrder(page.getPriorityOrder())
                                        .build())
                                .toList()
                )
                .build();
    }

    public PhotobookDetailResponse getDetail(Long projectId) {
        Photobook photobook = photobookRepository.findByProjectId(projectId)
                .orElseThrow(() -> new EntityNotFoundException("포토북을 찾을 수 없습니다. projectId=" + projectId));

        List<PhotobookPage> pages = photobookPageRepository.findAllByPhotobookIdOrderByPageNumberAsc(photobook.getId());

        return PhotobookDetailResponse.builder()
                .photobookId(photobook.getId())
                .projectId(projectId)
                .title(photobook.getTitle())
                .bookSpecUid(photobook.getBookSpecUid())
                .sweetbookBookUid(photobook.getSweetbookBookUid())
                .status(photobook.getStatus().name())
                .finalPageCount(photobook.getFinalPageCount())
                .lastErrorMessage(photobook.getLastErrorMessage())
                .pages(
                        pages.stream()
                                .map(page -> PhotobookPagePreviewResponse.builder()
                                        .curatedImageId(page.getCuratedImage().getId())
                                        .pageNumber(page.getPageNumber())
                                        .pageRole(page.getPageRole().name())
                                        .imageUrl(page.getImageSourceUrl())
                                        .caption(page.getCaption())
                                        .priorityOrder(page.getPriorityOrder())
                                        .build())
                                .toList()
                )
                .build();
    }
}