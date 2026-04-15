package com.sweet_hjr.server.domain.photobook.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PhotobookOrderCreateRequest {

    @NotNull
    private Long userId;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer quantity;

    @NotBlank
    @Size(max = 100)
    private String recipientName;

    @NotBlank
    @Size(max = 20)
    private String recipientPhone;

    @NotBlank
    @Size(max = 10)
    private String postalCode;

    @NotBlank
    @Size(max = 200)
    private String address1;

    @Size(max = 200)
    private String address2;

    @Size(max = 200)
    private String memo;

    @Size(max = 100)
    private String requestKey;

    public String resolveIdempotencyKey(Long projectId, Long photobookId, Long userId) {
        if (requestKey != null && !requestKey.isBlank()) {
            return requestKey;
        }
        return "photobook-order-" + projectId + "-" + photobookId + "-" + userId + "-" + System.currentTimeMillis();
    }

    public String buildExternalRef(Long projectId, Long photobookId, Long userId) {
        return "project-" + projectId + "-photobook-" + photobookId + "-user-" + userId;
    }
}