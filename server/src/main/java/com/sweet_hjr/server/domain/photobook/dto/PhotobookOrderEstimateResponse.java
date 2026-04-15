package com.sweet_hjr.server.domain.photobook.dto;

import com.sweet_hjr.server.domain.photobook.client.SweetbookApiModels;
import com.sweet_hjr.server.domain.photobook.entity.Photobook;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PhotobookOrderEstimateResponse {

    private Long projectId;
    private Long photobookId;
    private String photobookTitle;
    private String sweetbookBookUid;
    private Integer quantity;

    private BigDecimal totalProductAmount;
    private BigDecimal totalShippingFee;
    private BigDecimal totalPackagingFee;
    private BigDecimal totalAmount;
    private BigDecimal paidCreditAmount;
    private BigDecimal creditBalance;
    private Boolean creditSufficient;
    private String currency;

    public static PhotobookOrderEstimateResponse from(
            Long projectId,
            Photobook photobook,
            Integer quantity,
            SweetbookApiModels.OrderEstimateData data
    ) {
        return PhotobookOrderEstimateResponse.builder()
                .projectId(projectId)
                .photobookId(photobook.getId())
                .photobookTitle(photobook.getTitle())
                .sweetbookBookUid(photobook.getSweetbookBookUid())
                .quantity(quantity)
                .totalProductAmount(data.getTotalProductAmount())
                .totalShippingFee(data.getTotalShippingFee())
                .totalPackagingFee(data.getTotalPackagingFee())
                .totalAmount(data.getTotalAmount())
                .paidCreditAmount(data.getPaidCreditAmount())
                .creditBalance(data.getCreditBalance())
                .creditSufficient(data.getCreditSufficient())
                .currency(data.getCurrency())
                .build();
    }
}