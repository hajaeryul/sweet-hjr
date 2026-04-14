package com.sweet_hjr.server.domain.photobook.service;

import com.sweet_hjr.server.domain.photobook.client.SweetbookApiModels.BookSpecItem;
import com.sweet_hjr.server.domain.photobook.client.SweetbookApiModels.TemplateItem;
import com.sweet_hjr.server.domain.photobook.client.SweetbookClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SweetbookCatalogService {

    private final SweetbookClient sweetbookClient;

    public BookSpecItem getRequiredBookSpec(String bookSpecUid) {
        return sweetbookClient.getBookSpec(bookSpecUid);
    }

    public void validateTemplate(String bookSpecUid, String templateUid, String templateKind) {
        List<TemplateItem> templates = sweetbookClient.getTemplates(bookSpecUid, templateKind);
        boolean exists = templates.stream()
                .anyMatch(template -> templateUid.equals(template.getTemplateUid()));

        if (!exists) {
            throw new IllegalArgumentException(
                    "해당 판형에 맞는 " + templateKind + " 템플릿이 아닙니다. templateUid=" + templateUid
            );
        }
    }

    public int calculateExpectedFinalPageCount(String bookSpecUid, int contentPageCount) {
        BookSpecItem spec = getRequiredBookSpec(bookSpecUid);

        int min = spec.getPageMin();
        int max = spec.getPageMax();
        int increment = spec.getPageIncrement();

        int calculated = Math.max(min, contentPageCount);

        if ((calculated - min) % increment != 0) {
            int remainder = (calculated - min) % increment;
            calculated += (increment - remainder);
        }

        if (calculated > max) {
            throw new IllegalStateException("큐레이션된 이미지 수가 판형 최대 페이지 수를 초과합니다.");
        }

        return calculated;
    }
}