package com.sweet_hjr.server.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppUploadProperties.class)
public class AppUploadConfig {
}