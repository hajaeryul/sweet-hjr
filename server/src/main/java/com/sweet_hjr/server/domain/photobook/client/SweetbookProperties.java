package com.sweet_hjr.server.domain.photobook.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "sweetbook")
public class SweetbookProperties {
    private String baseUrl;
    private String apiKey;
}