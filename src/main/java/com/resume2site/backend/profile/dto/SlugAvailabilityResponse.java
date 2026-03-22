package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "SlugAvailabilityResponse", description = "Result of checking whether a requested public slug is valid and currently available.")
public record SlugAvailabilityResponse(
        @Schema(description = "Normalized slug value that was checked", example = "alice-johnson")
        String value,
        @Schema(description = "Whether the slug satisfies format/reserved-word validation", example = "true")
        boolean valid,
        @Schema(description = "Whether the slug is not currently used by another profile", example = "true")
        boolean available,
        @Schema(description = "Human-readable availability or validation message", example = "Slug is available")
        String message,
        @Schema(description = "Alternative slug suggestions when the requested slug is invalid or taken")
        List<String> suggestions
) {
}
