package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProfileLinkResponse", description = "External link shown on the profile, such as GitHub, LinkedIn, or a personal site.")
public record ProfileLinkResponse(
        @Schema(description = "Link id", example = "101")
        Long id,
        @Schema(description = "Display label for the link", example = "GitHub")
        String label,
        @Schema(description = "Fully qualified URL rendered by the frontend", example = "https://github.com/alice")
        String url,
        @Schema(description = "Order within the links collection", example = "0")
        Integer sortOrder
) {
}
