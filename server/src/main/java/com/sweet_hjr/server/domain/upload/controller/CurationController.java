package com.sweet_hjr.server.domain.upload.controller;

import com.sweet_hjr.server.domain.upload.dto.*;
import com.sweet_hjr.server.domain.upload.service.CurationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/curation")
public class CurationController {

    private final CurationService curationService;

    @GetMapping("/groups")
    public List<CurationGroupSummaryResponse> getProjectGroups(
            @PathVariable Long projectId
    ) {
        return curationService.getProjectGroups(projectId);
    }

    @GetMapping("/groups/{imageGroupId}/items")
    public List<CurationGroupItemResponse> getGroupItems(
            @PathVariable Long projectId,
            @PathVariable Long imageGroupId
    ) {
        return curationService.getGroupItems(projectId, imageGroupId);
    }

    @PostMapping("/images")
    public CurateImageResponse curateImage(
            @PathVariable Long projectId,
            @Valid @RequestBody CurateImageRequest request
    ) {
        return curationService.curateImage(projectId, request);
    }

    @GetMapping("/preview")
    public List<CuratedImagePreviewResponse> getCuratedImages(
            @PathVariable Long projectId
    ) {
        return curationService.getCuratedImages(projectId);
    }
}