package com.resume2site.backend.profile.dto;

import com.resume2site.backend.common.validation.ValidSlug;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateProfileSlugRequest", description = "Payload used to change the slug of an already published profile.")
public record UpdateProfileSlugRequest(
        @Schema(description = "New public slug to assign to the published profile", example = "alice-johnson-dev")
        @NotBlank(message = "slug is required")
        @Size(min = 3, max = 40, message = "slug must be between 3 and 40 characters")
        @ValidSlug
        String slug
) {
}
