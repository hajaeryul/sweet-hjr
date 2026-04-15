package com.sweet_hjr.server.domain.photobook.service;

import com.sweet_hjr.server.domain.photobook.client.SweetbookApiModels;
import com.sweet_hjr.server.domain.photobook.client.SweetbookClient;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookOrderCreateRequest;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookOrderCreateResponse;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookOrderEstimateRequest;
import com.sweet_hjr.server.domain.photobook.dto.PhotobookOrderEstimateResponse;
import com.sweet_hjr.server.domain.photobook.entity.Photobook;
import com.sweet_hjr.server.domain.photobook.entity.PhotobookStatus;
import com.sweet_hjr.server.domain.photobook.repository.PhotobookRepository;
import com.sweet_hjr.server.domain.project.entity.Project;
import com.sweet_hjr.server.domain.project.repository.ProjectRepository;
import com.sweet_hjr.server.domain.user.entity.User;
import com.sweet_hjr.server.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotobookOrderService {

    private final ProjectRepository projectRepository;
    private final PhotobookRepository photobookRepository;
    private final UserRepository userRepository;
    private final SweetbookClient sweetbookClient;

    @Transactional(readOnly = true)
    public PhotobookOrderEstimateResponse estimateOrder(Long projectId, PhotobookOrderEstimateRequest request) {
        Project project = getProject(projectId);
        validateProjectOrderWindow(project);

        Photobook photobook = getPhotobook(projectId);
        validateFinalizedPhotobook(photobook);

        int quantity = resolveQuantity(request.getQuantity());

        SweetbookApiModels.OrderEstimateRequest estimateRequest =
                buildEstimateRequest(photobook.getSweetbookBookUid(), quantity);

        SweetbookApiModels.OrderEstimateData estimateData = sweetbookClient.estimateOrder(estimateRequest);

        return PhotobookOrderEstimateResponse.from(projectId, photobook, quantity, estimateData);
    }

    public PhotobookOrderCreateResponse createOrder(Long projectId, PhotobookOrderCreateRequest request) {
        Project project = getProject(projectId);
        validateProjectOrderWindow(project);

        Photobook photobook = getPhotobook(projectId);
        validateFinalizedPhotobook(photobook);

        User user = getUser(request.getUserId());
        int quantity = resolveQuantity(request.getQuantity());

        SweetbookApiModels.OrderCreateRequest orderRequest = buildOrderRequest(
                photobook.getSweetbookBookUid(),
                quantity,
                request,
                request.buildExternalRef(projectId, photobook.getId(), user.getId())
        );

        SweetbookApiModels.OrderCreateData orderData = sweetbookClient.createOrder(
                orderRequest,
                request.resolveIdempotencyKey(projectId, photobook.getId(), user.getId())
        );

        return PhotobookOrderCreateResponse.from(projectId, photobook, user, quantity, orderData);
    }

    private SweetbookApiModels.OrderEstimateRequest buildEstimateRequest(String bookUid, int quantity) {
        SweetbookApiModels.OrderItemRequest item = new SweetbookApiModels.OrderItemRequest();
        item.setBookUid(bookUid);
        item.setQuantity(quantity);

        SweetbookApiModels.OrderEstimateRequest request = new SweetbookApiModels.OrderEstimateRequest();
        request.setItems(List.of(item));
        return request;
    }

    private SweetbookApiModels.OrderCreateRequest buildOrderRequest(
            String bookUid,
            int quantity,
            PhotobookOrderCreateRequest source,
            String externalRef
    ) {
        SweetbookApiModels.OrderItemRequest item = new SweetbookApiModels.OrderItemRequest();
        item.setBookUid(bookUid);
        item.setQuantity(quantity);

        SweetbookApiModels.OrderShippingRequest shipping = new SweetbookApiModels.OrderShippingRequest();
        shipping.setRecipientName(source.getRecipientName());
        shipping.setRecipientPhone(source.getRecipientPhone());
        shipping.setPostalCode(source.getPostalCode());
        shipping.setAddress1(source.getAddress1());
        shipping.setAddress2(source.getAddress2());
        shipping.setMemo(source.getMemo());

        SweetbookApiModels.OrderCreateRequest request = new SweetbookApiModels.OrderCreateRequest();
        request.setItems(List.of(item));
        request.setShipping(shipping);
        request.setExternalRef(externalRef);
        return request;
    }

    private Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트를 찾을 수 없습니다. id=" + projectId));
    }

    private Photobook getPhotobook(Long projectId) {
        return photobookRepository.findByProjectId(projectId)
                .orElseThrow(() -> new EntityNotFoundException("포토북을 찾을 수 없습니다. projectId=" + projectId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. id=" + userId));
    }

    private void validateProjectOrderWindow(Project project) {
        LocalDateTime now = LocalDateTime.now();

        if (project.getOrderStartAt() == null || project.getOrderEndAt() == null) {
            throw new IllegalStateException("주문 기간이 설정되지 않은 프로젝트입니다.");
        }

        if (now.isBefore(project.getOrderStartAt()) || now.isAfter(project.getOrderEndAt())) {
            throw new IllegalStateException("현재 주문 가능한 기간이 아닙니다.");
        }
    }

    private void validateFinalizedPhotobook(Photobook photobook) {
        if (photobook.getStatus() != PhotobookStatus.FINALIZED) {
            throw new IllegalStateException("FINALIZED 상태의 포토북만 주문할 수 있습니다.");
        }

        if (photobook.getSweetbookBookUid() == null || photobook.getSweetbookBookUid().isBlank()) {
            throw new IllegalStateException("주문 가능한 책 UID가 없습니다.");
        }
    }

    private int resolveQuantity(Integer quantity) {
        if (quantity == null) {
            return 1;
        }
        if (quantity < 1 || quantity > 100) {
            throw new IllegalArgumentException("주문 수량은 1~100 사이여야 합니다.");
        }
        return quantity;
    }
}