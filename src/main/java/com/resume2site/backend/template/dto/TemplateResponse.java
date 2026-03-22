package com.resume2site.backend.template.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "TemplateResponse", description = "Frontend-renderable template metadata used for preview selection and public portfolio rendering.")
public record TemplateResponse(
        @Schema(description = "Template id", example = "1")
        Long id,
        @Schema(description = "Stable template code used internally by the frontend to map to a template component", example = "minimal-dev")
        String code,
        @Schema(description = "Human-friendly template name", example = "Minimal Developer")
        String name,
        @Schema(description = "Short marketing description shown in the template picker", example = "Clean single-column template focused on early-career developers.")
        String description,
        @Schema(description = "Preview image URL if available", example = "https://cdn.resume2site.dev/templates/minimal-dev.png")
        String previewImageUrl,
        @Schema(description = "Template category used for grouping/filtering in the UI", example = "developer")
        String category,
        @Schema(description = "Primary accent color used by the template", example = "#111827")
        String accentColor,
        @Schema(description = "Short feature bullets suitable for preview cards")
        List<String> features,
        @Schema(description = "Order in which templates should be displayed", example = "1")
        Integer sortOrder
) {
}
