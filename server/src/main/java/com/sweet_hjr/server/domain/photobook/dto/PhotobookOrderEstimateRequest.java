package com.sweet_hjr.server.domain.photobook.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PhotobookOrderEstimateRequest {

    @Min(1)
    @Max(100)
    private Integer quantity = 1;
}