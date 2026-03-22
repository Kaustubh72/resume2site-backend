package com.resume2site.backend.profile.dto;

import com.resume2site.backend.common.validation.ValidSlug;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "PublishProfileRequest", description = "Payload used when a profile is first published or republished to a public slug.")
public record PublishProfileRequest(
        @Schema(description = "Requested public slug. Must be lowercase, unique, non-reserved, and 3-40 characters.", example = "alice-johnson")
        @NotBlank(message = "slug is required")
        @Size(min = 3, max = 40, message = "slug must be between 3 and 40 characters")
        @ValidSlug
        String slug
) {
}
