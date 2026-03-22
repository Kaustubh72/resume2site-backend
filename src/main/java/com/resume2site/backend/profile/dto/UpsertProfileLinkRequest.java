package com.resume2site.backend.profile.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpsertProfileLinkRequest(
        @NotBlank(message = "label is required")
        @Size(max = 100, message = "label must be at most 100 characters")
        String label,
        @NotBlank(message = "url is required")
        @Size(max = 500, message = "url must be at most 500 characters")
        @Pattern(regexp = "^(https?://).+", message = "url must start with http:// or https://")
        String url,
        @NotNull(message = "sortOrder is required")
        @Min(value = 0, message = "sortOrder must be 0 or greater")
        Integer sortOrder
) {
}
