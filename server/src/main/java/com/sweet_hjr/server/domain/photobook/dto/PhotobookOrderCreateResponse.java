package com.sweet_hjr.server.domain.photobook.dto;

import com.sweet_hjr.server.domain.photobook.client.SweetbookApiModels;
import com.sweet_hjr.server.domain.photobook.entity.Photobook;
import com.sweet_hjr.server.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class PhotobookOrderCreateResponse {

    private Long projectId;
    private Long photobookId;
    private Long userId;
    private String userNickname;

    private String orderUid;
    private Integer orderStatus;
    private String orderStatusDisplay;
    private Boolean isTest;

    private BigDecimal totalProductAmount;
    private BigDecimal totalShippingFee;
    private BigDecimal totalPackagingFee;
    private BigDecimal totalAmount;
    private BigDecimal paidCreditAmount;
    private BigDecimal creditBalanceAfter;

    private String recipientName;
    private String recipientPhone;
    private String postalCode;
    private String address1;
    private String address2;
    private String shippingMemo;
    private String orderedAt;

    private List<OrderItemSummary> items;

    public static PhotobookOrderCreateResponse from(
            Long projectId,
            Photobook photobook,
            User user,
            Integer quantity,
            SweetbookApiModels.OrderCreateData data
    ) {
        return PhotobookOrderCreateResponse.builder()
                .projectId(projectId)
                .photobookId(photobook.getId())
                .userId(user.getId())
                .userNickname(user.getNickname())
                .orderUid(data.getOrderUid())
                .orderStatus(data.getOrderStatus())
                .orderStatusDisplay(data.getOrderStatusDisplay())
                .isTest(data.getIsTest())
                .totalProductAmount(data.getTotalProductAmount())
                .totalShippingFee(data.getTotalShippingFee())
                .totalPackagingFee(data.getTotalPackagingFee())
                .totalAmount(data.getTotalAmount())
                .paidCreditAmount(data.getPaidCreditAmount())
                .creditBalanceAfter(data.getCreditBalanceAfter())
                .recipientName(data.getRecipientName())
                .recipientPhone(data.getRecipientPhone())
                .postalCode(data.getPostalCode())
                .address1(data.getAddress1())
                .address2(data.getAddress2())
                .shippingMemo(data.getShippingMemo())
                .orderedAt(data.getOrderedAt())
                .items(
                        data.getItems().stream()
                                .map(item -> OrderItemSummary.builder()
                                        .itemUid(item.getItemUid())
                                        .bookUid(item.getBookUid())
                                        .bookSpecUid(item.getBookSpecUid())
                                        .bookSpecName(item.getBookSpecName())
                                        .quantity(item.getQuantity())
                                        .pageCount(item.getPageCount())
                                        .unitPrice(item.getUnitPrice())
                                        .itemAmount(item.getItemAmount())
                                        .itemStatus(item.getItemStatus())
                                        .itemStatusDisplay(item.getItemStatusDisplay())
                                        .build())
                                .toList()
                )
                .build();
    }

    @Getter
    @Builder
    public static class OrderItemSummary {
        private String itemUid;
        private String bookUid;
        private String bookSpecUid;
        private String bookSpecName;
        private Integer quantity;
        private Integer pageCount;
        private BigDecimal unitPrice;
        private BigDecimal itemAmount;
        private Integer itemStatus;
        private String itemStatusDisplay;
    }
}