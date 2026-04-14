package com.sweet_hjr.server.domain.photobook.service;

import com.sweet_hjr.server.domain.photobook.client.SweetbookApiModels.TemplateDetailData;
import com.sweet_hjr.server.domain.photobook.client.SweetbookApiModels.TemplateItem;
import com.sweet_hjr.server.domain.photobook.client.SweetbookApiModels.TemplateParameterDefinition;
import com.sweet_hjr.server.domain.photobook.client.SweetbookClient;
import com.sweet_hjr.server.domain.photobook.dto.TemplateDetailResponse;
import com.sweet_hjr.server.domain.photobook.dto.TemplateParameterResponse;
import com.sweet_hjr.server.domain.photobook.dto.TemplateSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SweetbookTemplateQueryService {

    private final SweetbookClient sweetbookClient;

    public List<TemplateSummaryResponse> getTemplates(String bookSpecUid, String templateKind) {
        return sweetbookClient.getTemplates(bookSpecUid, templateKind).stream()
                .map(this::toSummaryResponse)
                .sorted(Comparator
                        .comparing(TemplateSummaryResponse::getRequiredParameterCount)
                        .thenComparing(TemplateSummaryResponse::getTemplateName, Comparator.nullsLast(String::compareTo)))
                .toList();
    }

    public TemplateDetailResponse getTemplateDetail(String templateUid) {
        TemplateDetailData detail = sweetbookClient.getTemplateDetail(templateUid);

        List<TemplateParameterResponse> parameters = detail.getParameters() == null || detail.getParameters().getDefinitions() == null
                ? List.of()
                : detail.getParameters().getDefinitions().entrySet().stream()
                .map(this::toParameterResponse)
                .sorted(Comparator
                        .comparing((TemplateParameterResponse p) -> !Boolean.TRUE.equals(p.getRequired()))
                        .thenComparing(TemplateParameterResponse::getKey))
                .toList();

        int requiredCount = (int) parameters.stream()
                .filter(p -> Boolean.TRUE.equals(p.getRequired()))
                .count();

        return TemplateDetailResponse.builder()
                .templateUid(detail.getTemplateUid())
                .templateName(detail.getTemplateName())
                .description(detail.getDescription())
                .theme(detail.getTheme())
                .templateKind(detail.getTemplateKind())
                .bookSpecUid(detail.getBookSpecUid())
                .status(detail.getStatus())
                .layoutThumbnailUrl(detail.getThumbnails() != null ? detail.getThumbnails().getLayout() : null)
                .mockupThumbnailUrl(detail.getThumbnails() != null ? detail.getThumbnails().getMockup() : null)
                .requiredParameterCount(requiredCount)
                .parameters(parameters)
                .build();
    }

    private TemplateSummaryResponse toSummaryResponse(TemplateItem item) {
        int requiredCount = 0;

        try {
            TemplateDetailData detail = sweetbookClient.getTemplateDetail(item.getTemplateUid());
            if (detail.getParameters() != null && detail.getParameters().getDefinitions() != null) {
                requiredCount = (int) detail.getParameters().getDefinitions().values().stream()
                        .filter(def -> Boolean.TRUE.equals(def.getRequired()))
                        .count();
            }
        } catch (Exception ignored) {
            // 목록은 최대한 보여주고, 상세 조회에서 정확히 확인
        }

        return TemplateSummaryResponse.builder()
                .templateUid(item.getTemplateUid())
                .templateName(item.getTemplateName())
                .description(item.getDescription())
                .theme(item.getTheme())
                .templateKind(item.getTemplateKind())
                .bookSpecUid(item.getBookSpecUid())
                .status(item.getStatus())
                .layoutThumbnailUrl(item.getThumbnails() != null ? item.getThumbnails().getLayout() : null)
                .requiredParameterCount(requiredCount)
                .build();
    }

    private TemplateParameterResponse toParameterResponse(Map.Entry<String, TemplateParameterDefinition> entry) {
        TemplateParameterDefinition def = entry.getValue();

        return TemplateParameterResponse.builder()
                .key(entry.getKey())
                .binding(def.getBinding())
                .required(def.getRequired())
                .type(def.getType())
                .label(def.getLabel())
                .description(def.getDescription())
                .build();
    }
}