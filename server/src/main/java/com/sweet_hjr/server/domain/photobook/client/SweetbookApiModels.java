package com.sweet_hjr.server.domain.photobook.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class SweetbookApiModels {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SweetbookEnvelope<T> {
        private boolean success;
        private String message;
        private T data;
        private List<String> errors;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BookCreateRequest {
        private String title;
        private String bookSpecUid;
        private String specProfileUid;
        private String externalRef;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BookCreateData {
        private String bookUid;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BookSpecItem {
        private String bookSpecUid;
        private String name;
        private Integer pageMin;
        private Integer pageMax;
        private Integer pageIncrement;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TemplateItem {
        private String templateUid;
        private String templateName;
        private String description;
        private String theme;
        private String templateKind;
        private String bookSpecUid;
        private String status;
        private TemplateThumbnails thumbnails;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TemplateThumbnails {
        private String layout;
        private String mockup;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TemplatesData {
        private List<TemplateItem> templates;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TemplateDetailData {
        private String templateUid;
        private String templateName;
        private String description;
        private String theme;
        private String templateKind;
        private String bookSpecUid;
        private String status;
        private TemplateThumbnails thumbnails;
        private TemplateParameters parameters;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TemplateParameters {
        private Map<String, TemplateParameterDefinition> definitions;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TemplateParameterDefinition {
        private String binding;
        private Boolean required;
        private String type;
        private String label;
        private String description;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PhotoUploadData {
        private String fileName;
        private String originalName;
        private Long size;
        private String mimeType;
        private String uploadedAt;
        private Boolean isDuplicate;
        private String hash;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class InsertResultData {
        private String result;
        private String breakBefore;
        private Integer pageNum;
        private String pageSide;
        private Integer pageCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class FinalizationData {
        private String result;
        private Integer pageCount;
        private String finalizedAt;
    }
}