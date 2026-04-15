package com.sweet_hjr.server.domain.photobook.controller;

import com.sweet_hjr.server.domain.photobook.dto.PhotobookOrderCreateRequest;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookOrderCreateResponse;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookOrderEstimateRequest;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookOrderEstimateResponse;
import com.sweet_hjr.server.domain.photobook.service.PhotobookOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/photobooks")
public class PhotobookOrderController {

    private final PhotobookOrderService photobookOrderService;

    @PostMapping("/order-estimate")
    public PhotobookOrderEstimateResponse estimateOrder(
            @PathVariable Long projectId,
            @Valid @RequestBody PhotobookOrderEstimateRequest request
    ) {
        return photobookOrderService.estimateOrder(projectId, request);
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public PhotobookOrderCreateResponse createOrder(
            @PathVariable Long projectId,
            @Valid @RequestBody PhotobookOrderCreateRequest request
    ) {
        return photobookOrderService.createOrder(projectId, request);
    }
}