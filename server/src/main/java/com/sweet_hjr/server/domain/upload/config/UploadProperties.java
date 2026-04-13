package com.sweet_hjr.server.domain.upload.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "app.upload")
public class UploadProperties {

    private String baseDir;

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}