package com.sweet_hjr.server.domain.photobook.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweet_hjr.server.domain.photobook.client.SweetbookApiModels.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SweetbookClient {

    private final RestClient sweetbookRestClient;
    private final ObjectMapper objectMapper;

    public BookCreateData createBook(String title, String bookSpecUid, String specProfileUid, String externalRef) {
        BookCreateRequest request = new BookCreateRequest();
        request.setTitle(title);
        request.setBookSpecUid(bookSpecUid);
        request.setSpecProfileUid(specProfileUid);
        request.setExternalRef(externalRef);

        SweetbookEnvelope<BookCreateData> response = sweetbookRestClient.post()
                .uri("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        validateResponse(response);

        if (response.getData() == null) {
            throw new IllegalStateException("책 생성 응답 데이터가 비어 있습니다.");
        }

        return response.getData();
    }

    public BookSpecItem getBookSpec(String bookSpecUid) {
        SweetbookEnvelope<BookSpecItem> response = sweetbookRestClient.get()
                .uri("/book-specs/{bookSpecUid}", bookSpecUid)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        validateResponse(response);

        if (response.getData() == null) {
            throw new IllegalStateException("판형 상세 응답 데이터가 비어 있습니다. bookSpecUid=" + bookSpecUid);
        }

        return response.getData();
    }

    public List<TemplateItem> getTemplates(String bookSpecUid, String templateKind) {
        SweetbookEnvelope<TemplatesData> response = sweetbookRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/templates")
                        .queryParam("bookSpecUid", bookSpecUid)
                        .queryParam("templateKind", templateKind)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        validateResponse(response);

        if (response.getData() == null || response.getData().getTemplates() == null) {
            throw new IllegalStateException("템플릿 목록 응답 데이터가 비어 있습니다.");
        }

        return response.getData().getTemplates();
    }

    public TemplateDetailData getTemplateDetail(String templateUid) {
        SweetbookEnvelope<TemplateDetailData> response = sweetbookRestClient.get()
                .uri("/templates/{templateUid}", templateUid)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        validateResponse(response);

        if (response.getData() == null) {
            throw new IllegalStateException("템플릿 상세 응답 데이터가 비어 있습니다. templateUid=" + templateUid);
        }

        return response.getData();
    }

    public void addCoverUsingUrl(String bookUid, String templateUid, Map<String, Object> parameters) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("templateUid", templateUid);
        body.add("parameters", toJson(parameters));

        SweetbookEnvelope<InsertResultData> response = sweetbookRestClient.post()
                .uri("/books/{bookUid}/cover", bookUid)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        validateResponse(response);
    }

    public InsertResultData addContentUsingUrl(String bookUid, String templateUid, Map<String, Object> parameters, String breakBefore) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("templateUid", templateUid);
        body.add("parameters", toJson(parameters));

        SweetbookEnvelope<InsertResultData> response = sweetbookRestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/{bookUid}/contents")
                        .queryParam("breakBefore", breakBefore)
                        .build(bookUid))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        validateResponse(response);

        if (response.getData() == null) {
            throw new IllegalStateException("내지 추가 응답 데이터가 비어 있습니다.");
        }

        return response.getData();
    }

    public FinalizationData finalizeBook(String bookUid) {
        SweetbookEnvelope<FinalizationData> response = sweetbookRestClient.post()
                .uri("/books/{bookUid}/finalization", bookUid)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        validateResponse(response);

        if (response.getData() == null) {
            throw new IllegalStateException("최종화 응답 데이터가 비어 있습니다.");
        }

        return response.getData();
    }

    public PhotoUploadData uploadPhoto(String bookUid, String absoluteFilePath) {
        try {
            Path path = Paths.get(absoluteFilePath);

            if (!Files.exists(path) || !Files.isReadable(path)) {
                throw new IllegalArgumentException("업로드할 파일을 찾을 수 없거나 읽을 수 없습니다. path=" + absoluteFilePath);
            }

            byte[] fileBytes = Files.readAllBytes(path);

            String fileName = path.getFileName().toString();
            MediaType mediaType = resolveImageMediaType(fileName, path);

            ByteArrayResource resource = new ByteArrayResource(fileBytes) {
                @Override
                public String getFilename() {
                    return fileName;
                }
            };

            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.setContentDispositionFormData("file", fileName);
            fileHeaders.setContentType(mediaType);

            HttpEntity<ByteArrayResource> filePart = new HttpEntity<>(resource, fileHeaders);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", filePart);

            SweetbookEnvelope<PhotoUploadData> response = sweetbookRestClient.post()
                    .uri("/books/{bookUid}/photos", bookUid)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            validateResponse(response);

            if (response.getData() == null || response.getData().getFileName() == null) {
                throw new IllegalStateException("사진 업로드 응답 데이터가 비어 있습니다.");
            }

            return response.getData();

        } catch (IOException e) {
            throw new IllegalStateException("업로드 파일을 읽는 중 오류가 발생했습니다. path=" + absoluteFilePath, e);
        }
    }

    private MediaType resolveImageMediaType(String fileName, Path path) throws IOException {
        String detected = Files.probeContentType(path);

        if (detected != null) {
            try {
                return MediaType.parseMediaType(detected);
            } catch (Exception ignored) {
            }
        }

        String lower = fileName.toLowerCase();

        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        }
        if (lower.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (lower.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        }
        if (lower.endsWith(".webp")) {
            return MediaType.parseMediaType("image/webp");
        }

        throw new IllegalArgumentException("지원하지 않는 이미지 확장자입니다. fileName=" + fileName);
    }

    private void validateResponse(SweetbookEnvelope<?> response) {
        if (response == null) {
            throw new IllegalStateException("Sweetbook 응답이 비어 있습니다.");
        }

        if (!response.isSuccess()) {
            String message = response.getMessage();

            if (response.getErrors() != null && !response.getErrors().isEmpty()) {
                message += " / " + String.join(", ", response.getErrors());
            }

            throw new IllegalStateException("Sweetbook API 호출 실패: " + message);
        }
    }

    private String toJson(Map<String, Object> parameters) {
        try {
            return objectMapper.writeValueAsString(parameters);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("parameters JSON 변환에 실패했습니다.", e);
        }
    }
}