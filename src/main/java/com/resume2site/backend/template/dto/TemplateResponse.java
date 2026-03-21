package com.resume2site.backend.template.dto;

import java.util.List;

public record TemplateResponse(
        Long id,
        String code,
        String name,
        String description,
        String previewImageUrl,
        String category,
        String accentColor,
        List<String> features,
        Integer sortOrder
) {
}
