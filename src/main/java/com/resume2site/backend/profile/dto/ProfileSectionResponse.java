package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProfileSectionResponse", description = "Visibility and ordering configuration for one logical profile section.")
public record ProfileSectionResponse(
        @Schema(description = "Stable section key understood by the frontend", example = "projects")
        String sectionKey,
        @Schema(description = "User-facing display label for the section", example = "Projects")
        String displayName,
        @Schema(description = "Whether the section should be rendered publicly", example = "true")
        boolean visible,
        @Schema(description = "Display order among sections", example = "4")
        Integer sortOrder
) {
}
