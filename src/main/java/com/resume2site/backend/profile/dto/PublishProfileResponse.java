package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PublishProfileResponse", description = "Result returned after a successful publish, republish, or slug update.")
public record PublishProfileResponse(
        @Schema(description = "Profile id", example = "10")
        Long profileId,
        @Schema(description = "Final stored public slug", example = "alice-johnson")
        String slug,
        @Schema(description = "Current publication status after the operation", example = "PUBLISHED")
        String publicationStatus,
        @Schema(description = "Template id used for the published profile", example = "2")
        Long templateId,
        @Schema(description = "Frontend path where the public portfolio will be available", example = "/u/alice-johnson")
        String publicUrl
) {
}
