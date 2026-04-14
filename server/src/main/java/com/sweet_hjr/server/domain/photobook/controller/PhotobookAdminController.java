package com.sweet_hjr.server.domain.photobook.controller;

import com.sweet_hjr.server.domain.photobook.dto.PhotobookCreateRequest;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookCreateResponse;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookDetailResponse;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookLayoutPreviewResponse;
import com.sweet_hjr.server.domain.photobook.dto.TemplateDetailResponse;
import com.sweet_hjr.server.domain.photobook.dto.TemplateSummaryResponse;
import com.sweet_hjr.server.domain.photobook.service.PhotobookAdminService;
import com.sweet_hjr.server.domain.photobook.service.PhotobookQueryService;
import com.sweet_hjr.server.domain.photobook.service.SweetbookTemplateQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PhotobookAdminController {

    private final PhotobookAdminService photobookAdminService;
    private final PhotobookQueryService photobookQueryService;
    private final SweetbookTemplateQueryService sweetbookTemplateQueryService;

    /**
     * 큐레이션 결과를 바탕으로 포토북 배치 미리보기
     */
    @GetMapping("/admin/projects/{projectId}/photobook-layout")
    public PhotobookLayoutPreviewResponse previewLayout(
            @PathVariable Long projectId,
            @RequestParam String bookSpecUid
    ) {
        return photobookQueryService.previewLayout(projectId, bookSpecUid);
    }

    /**
     * 포토북 생성
     * - 내부 DB 저장
     * - Sweetbook 책 생성
     * - cover 추가
     * - content 추가
     * - finalization
     */
    @PostMapping("/admin/projects/{projectId}/photobooks")
    public PhotobookCreateResponse createPhotobook(
            @PathVariable Long projectId,
            @RequestBody PhotobookCreateRequest request
    ) {
        return photobookAdminService.createPhotobook(projectId, request);
    }

    /**
     * 프로젝트에 연결된 포토북 상세 조회
     */
    @GetMapping("/admin/projects/{projectId}/photobooks")
    public PhotobookDetailResponse getPhotobookDetail(@PathVariable Long projectId) {
        return photobookQueryService.getDetail(projectId);
    }

    /**
     * Sweetbook 템플릿 목록 조회
     * 예:
     * /api/admin/sweetbook/templates?bookSpecUid=SQUAREBOOK_HC&templateKind=cover
     * /api/admin/sweetbook/templates?bookSpecUid=SQUAREBOOK_HC&templateKind=content
     */
    @GetMapping("/admin/sweetbook/templates")
    public List<TemplateSummaryResponse> getTemplates(
            @RequestParam String bookSpecUid,
            @RequestParam String templateKind
    ) {
        return sweetbookTemplateQueryService.getTemplates(bookSpecUid, templateKind);
    }

    /**
     * Sweetbook 템플릿 상세 조회
     * 예:
     * /api/admin/sweetbook/templates/1Es0DP4oARn8
     */
    @GetMapping("/admin/sweetbook/templates/{templateUid}")
    public TemplateDetailResponse getTemplateDetail(@PathVariable String templateUid) {
        return sweetbookTemplateQueryService.getTemplateDetail(templateUid);
    }
}