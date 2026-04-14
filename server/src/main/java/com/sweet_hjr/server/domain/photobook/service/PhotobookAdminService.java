package com.sweet_hjr.server.domain.photobook.service;

import com.sweet_hjr.server.domain.photobook.client.SweetbookApiModels.FinalizationData;
import com.sweet_hjr.server.domain.photobook.client.SweetbookApiModels.InsertResultData;
import com.sweet_hjr.server.domain.photobook.client.SweetbookClient;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookCreateRequest;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookCreateResponse;
import com.sweet_hjr.server.domain.photobook.entity.Photobook;
import com.sweet_hjr.server.domain.photobook.entity.PhotobookPage;
import com.sweet_hjr.server.domain.photobook.entity.PhotobookPageRole;
import com.sweet_hjr.server.domain.photobook.entity.PhotobookStatus;
import com.sweet_hjr.server.domain.photobook.repository.PhotobookPageRepository;
import com.sweet_hjr.server.domain.photobook.repository.PhotobookRepository;
import com.sweet_hjr.server.domain.project.entity.Project;
import com.sweet_hjr.server.domain.project.repository.ProjectRepository;
import com.sweet_hjr.server.domain.upload.entity.CuratedImage;
import com.sweet_hjr.server.domain.upload.repository.CuratedImageRepository;
import com.sweet_hjr.server.domain.user.entity.User;
import com.sweet_hjr.server.domain.user.repository.UserRepository;
import com.sweet_hjr.server.global.config.AppUploadProperties;
import com.sweet_hjr.server.domain.photobook.client.SweetbookApiModels.PhotoUploadData;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotobookAdminService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CuratedImageRepository curatedImageRepository;
    private final PhotobookRepository photobookRepository;
    private final PhotobookPageRepository photobookPageRepository;
    private final UserRoleGuard userRoleGuard;
    private final PhotobookLayoutService photobookLayoutService;
    private final SweetbookCatalogService sweetbookCatalogService;
    private final SweetbookClient sweetbookClient;
    private final PhotobookSyncLogService photobookSyncLogService;
    private final AppUploadProperties appUploadProperties;

    public PhotobookCreateResponse createPhotobook(Long projectId, PhotobookCreateRequest request) {
        userRoleGuard.assertAdmin(request.getAdminUserId());

        if (photobookRepository.existsByProjectId(projectId)) {
            throw new IllegalStateException("이미 생성된 포토북이 있습니다. projectId=" + projectId);
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트를 찾을 수 없습니다. id=" + projectId));

        User admin = userRepository.findById(request.getAdminUserId())
                .orElseThrow(() -> new EntityNotFoundException("관리자 사용자를 찾을 수 없습니다. id=" + request.getAdminUserId()));

        sweetbookCatalogService.getRequiredBookSpec(request.getBookSpecUid());
        sweetbookCatalogService.validateTemplate(request.getBookSpecUid(), request.getCoverTemplateUid(), "cover");
        sweetbookCatalogService.validateTemplate(request.getBookSpecUid(), request.getContentTemplateUid(), "content");

        PhotobookLayoutModels.LayoutResult layout = photobookLayoutService.buildLayout(projectId, request.getBookSpecUid());

        Photobook photobook = photobookRepository.save(
                Photobook.builder()
                        .project(project)
                        .title(request.getTitle())
                        .bookSpecUid(request.getBookSpecUid())
                        .specProfileUid(request.getSpecProfileUid())
                        .externalRef(request.buildExternalRef(projectId))
                        .status(PhotobookStatus.DRAFT)
                        .createdBy(admin)
                        .coverCuratedImageId(layout.getCover().getCuratedImageId())
                        .build()
        );

        try {
            String bookUid = createSweetbookBook(photobook);

            String coverFileName = addCover(
                    photobook,
                    layout.getCover(),
                    request.getCoverTemplateUid()
            );

            saveCoverPage(
                    photobook,
                    layout.getCover(),
                    request.getCoverTemplateUid(),
                    coverFileName
            );

            addContents(photobook, layout.getContents(), request.getContentTemplateUid());
            photobook.markContentsAdded();

            FinalizationData finalizationData = sweetbookClient.finalizeBook(bookUid);
            photobook.markFinalized(finalizationData.getPageCount());

            photobookSyncLogService.success(
                    photobook.getId(),
                    "FINALIZATION",
                    "bookUid=" + bookUid,
                    "pageCount=" + finalizationData.getPageCount()
            );

            return PhotobookCreateResponse.builder()
                    .photobookId(photobook.getId())
                    .projectId(projectId)
                    .sweetbookBookUid(photobook.getSweetbookBookUid())
                    .status(photobook.getStatus().name())
                    .finalPageCount(photobook.getFinalPageCount())
                    .build();

        } catch (Exception e) {
            photobook.markFailed(e.getMessage());
            photobookSyncLogService.fail(photobook.getId(), "CREATE_PHOTOBOOK", "projectId=" + projectId, e);
            throw e;
        }
    }

    private String createSweetbookBook(Photobook photobook) {
        var created = sweetbookClient.createBook(
                photobook.getTitle(),
                photobook.getBookSpecUid(),
                photobook.getSpecProfileUid(),
                photobook.getExternalRef()
        );

        photobook.markBookCreated(created.getBookUid());

        photobookSyncLogService.success(
                photobook.getId(),
                "CREATE_BOOK",
                "title=" + photobook.getTitle() + ", bookSpecUid=" + photobook.getBookSpecUid(),
                "bookUid=" + created.getBookUid()
        );

        return created.getBookUid();
    }

    private String addCover(
            Photobook photobook,
            PhotobookLayoutModels.LayoutPage cover,
            String coverTemplateUid
    ) {
        String uploadedFileName = uploadImageAndGetFileName(photobook, cover.getImageUrl());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("coverPhoto", uploadedFileName);
        parameters.put("dateRange", "2026 시즌");
        parameters.put("subtitle", photobook.getTitle());

        // 템플릿에 따라 있으면 쓰이고, 없으면 무시될 수 있음
        parameters.put("title", photobook.getTitle());

        if (cover.getCaption() != null && !cover.getCaption().isBlank()) {
            parameters.put("subtitle", cover.getCaption());
        }

        sweetbookClient.addCoverUsingUrl(
                photobook.getSweetbookBookUid(),
                coverTemplateUid,
                parameters
        );

        photobook.markCoverAdded();

        photobookSyncLogService.success(
                photobook.getId(),
                "ADD_COVER",
                "templateUid=" + coverTemplateUid,
                "uploadedFileName=" + uploadedFileName
        );

        return uploadedFileName;
    }

    private void saveCoverPage(Photobook photobook,
                               PhotobookLayoutModels.LayoutPage cover,
                               String coverTemplateUid,
                               String uploadedFileName) {
        CuratedImage curatedImage = curatedImageRepository.findById(cover.getCuratedImageId())
                .orElseThrow(() -> new EntityNotFoundException("표지 curatedImage를 찾을 수 없습니다."));

        photobookPageRepository.save(
                PhotobookPage.builder()
                        .photobook(photobook)
                        .curatedImage(curatedImage)
                        .pageRole(PhotobookPageRole.COVER)
                        .pageNumber(0)
                        .templateUid(coverTemplateUid)
                        .imageSourceUrl(cover.getImageUrl())
                        .sweetbookFileName(uploadedFileName)
                        .caption(cover.getCaption())
                        .priorityOrder(cover.getPriorityOrder())
                        .build()
        );
    }

    private void addContents(Photobook photobook, List<PhotobookLayoutModels.LayoutPage> pages, String contentTemplateUid) {
        for (PhotobookLayoutModels.LayoutPage page : pages) {
            String uploadedFileName = uploadImageAndGetFileName(photobook, page.getImageUrl());

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("photo1", uploadedFileName);

            if (page.getCaption() != null && !page.getCaption().isBlank()) {
                parameters.put("caption", page.getCaption());
                parameters.put("description", page.getCaption());
            }

            String breakBefore = "page";

            InsertResultData resultData = sweetbookClient.addContentUsingUrl(
                    photobook.getSweetbookBookUid(),
                    contentTemplateUid,
                    parameters,
                    breakBefore
            );

            CuratedImage curatedImage = curatedImageRepository.findById(page.getCuratedImageId())
                    .orElseThrow(() -> new EntityNotFoundException("본문 curatedImage를 찾을 수 없습니다. id=" + page.getCuratedImageId()));

            photobookPageRepository.save(
                    PhotobookPage.builder()
                            .photobook(photobook)
                            .curatedImage(curatedImage)
                            .pageRole(PhotobookPageRole.CONTENT)
                            .pageNumber(page.getPageNumber())
                            .templateUid(contentTemplateUid)
                            .imageSourceUrl(page.getImageUrl())
                            .sweetbookFileName(uploadedFileName)
                            .caption(page.getCaption())
                            .priorityOrder(page.getPriorityOrder())
                            .build()
            );

            photobookSyncLogService.success(
                    photobook.getId(),
                    "ADD_CONTENT",
                    "pageNumber=" + page.getPageNumber() + ", templateUid=" + contentTemplateUid,
                    "pageNum=" + resultData.getPageNum() + ", pageCount=" + resultData.getPageCount()
            );
        }
    }

    private String resolveAbsoluteFilePath(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("이미지 URL이 비어 있습니다.");
        }

        String normalized = imageUrl.replace("\\", "/");

        if (!normalized.startsWith("/uploads/")) {
            throw new IllegalArgumentException("지원하지 않는 이미지 경로입니다. imageUrl=" + imageUrl);
        }

        String relativePath = normalized.substring("/uploads/".length());
        Path absolutePath = Paths.get(appUploadProperties.getBaseDir(), relativePath);

        return absolutePath.toString();
    }

    private String uploadImageAndGetFileName(Photobook photobook, String imageUrl) {
        String absoluteFilePath = resolveAbsoluteFilePath(imageUrl);
        PhotoUploadData uploaded = sweetbookClient.uploadPhoto(photobook.getSweetbookBookUid(), absoluteFilePath);

        photobookSyncLogService.success(
                photobook.getId(),
                "UPLOAD_PHOTO",
                "filePath=" + absoluteFilePath,
                "fileName=" + uploaded.getFileName() + ", duplicate=" + uploaded.getIsDuplicate()
        );

        return uploaded.getFileName();
    }
}