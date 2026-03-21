package com.resume2site.backend.template.dto;

public record TemplateResponse(
        Long id,
        String code,
        String name,
        String description,
        String previewImageUrl,
        Integer sortOrder
) {
}
