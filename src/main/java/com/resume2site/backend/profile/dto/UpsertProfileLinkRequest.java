package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "UpsertProfileLinkRequest", description = "Payload used to create or update one profile link.")
public record UpsertProfileLinkRequest(
        @Schema(description = "User-facing link label", example = "GitHub")
        @NotBlank(message = "label is required")
        @Size(max = 100, message = "label must be at most 100 characters")
        String label,
        @Schema(description = "Fully qualified external URL", example = "https://github.com/alice")
        @NotBlank(message = "url is required")
        @Size(max = 500, message = "url must be at most 500 characters")
        @Pattern(regexp = "^(https?://).+", message = "url must start with http:// or https://")
        String url,
        @Schema(description = "Order within the links list", example = "0")
        @NotNull(message = "sortOrder is required")
        @Min(value = 0, message = "sortOrder must be 0 or greater")
        Integer sortOrder
) {
}
