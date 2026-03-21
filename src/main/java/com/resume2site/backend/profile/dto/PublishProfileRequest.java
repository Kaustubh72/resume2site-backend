package com.resume2site.backend.profile.dto;

import com.resume2site.backend.common.validation.ValidSlug;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublishProfileRequest(
        @NotBlank(message = "slug is required")
        @Size(min = 3, max = 40, message = "slug must be between 3 and 40 characters")
        @ValidSlug
        String slug
) {
}
