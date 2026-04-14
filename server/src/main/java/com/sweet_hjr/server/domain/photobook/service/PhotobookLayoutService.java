package com.sweet_hjr.server.domain.photobook.service;

import com.sweet_hjr.server.domain.upload.entity.CuratedImage;
import com.sweet_hjr.server.domain.upload.entity.FanUploadFile;
import com.sweet_hjr.server.domain.upload.repository.CuratedImageRepository;
import com.sweet_hjr.server.domain.upload.repository.FanUploadFileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PhotobookLayoutService {

    private final CuratedImageRepository curatedImageRepository;
    private final FanUploadFileRepository fanUploadFileRepository;
    private final SweetbookCatalogService sweetbookCatalogService;

    public PhotobookLayoutModels.LayoutResult buildLayout(Long projectId, String bookSpecUid) {
        List<CuratedImage> curatedImages =
                curatedImageRepository.findAllByProjectIdOrderByPriorityOrderAscCreatedAtAsc(projectId);

        if (curatedImages.isEmpty()) {
            throw new IllegalStateException("큐레이션된 이미지가 없어 포토북을 생성할 수 없습니다.");
        }

        CuratedImage coverCuratedImage = curatedImages.get(0);
        FanUploadFile coverFile = fanUploadFileRepository.findById(coverCuratedImage.getSourceImageId())
                .orElseThrow(() -> new EntityNotFoundException("표지 원본 이미지를 찾을 수 없습니다. id=" + coverCuratedImage.getSourceImageId()));

        PhotobookLayoutModels.LayoutPage cover = PhotobookLayoutModels.LayoutPage.builder()
                .curatedImageId(coverCuratedImage.getId())
                .sourceImageId(coverCuratedImage.getSourceImageId())
                .pageNumber(0)
                .pageRole("COVER")
                .imageUrl(coverFile.getFileUrl())
                .caption(coverCuratedImage.getSelectedReason())
                .priorityOrder(coverCuratedImage.getPriorityOrder())
                .build();

        List<PhotobookLayoutModels.LayoutPage> contentPages = new ArrayList<>();
        for (int i = 1; i < curatedImages.size(); i++) {
            CuratedImage curatedImage = curatedImages.get(i);

            FanUploadFile file = fanUploadFileRepository.findById(curatedImage.getSourceImageId())
                    .orElseThrow(() -> new EntityNotFoundException("본문 원본 이미지를 찾을 수 없습니다. id=" + curatedImage.getSourceImageId()));

            contentPages.add(
                    PhotobookLayoutModels.LayoutPage.builder()
                            .curatedImageId(curatedImage.getId())
                            .sourceImageId(curatedImage.getSourceImageId())
                            .pageNumber(i)
                            .pageRole("CONTENT")
                            .imageUrl(file.getFileUrl())
                            .caption(curatedImage.getSelectedReason())
                            .priorityOrder(curatedImage.getPriorityOrder())
                            .build()
            );
        }

        int expectedFinalPageCount = sweetbookCatalogService.calculateExpectedFinalPageCount(
                bookSpecUid,
                contentPages.size()
        );

        return PhotobookLayoutModels.LayoutResult.builder()
                .projectId(projectId)
                .cover(cover)
                .contents(contentPages)
                .expectedFinalPageCount(expectedFinalPageCount)
                .build();
    }
}