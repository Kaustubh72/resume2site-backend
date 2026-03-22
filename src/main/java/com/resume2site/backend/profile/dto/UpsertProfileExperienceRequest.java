package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Schema(name = "UpsertProfileExperienceRequest", description = "Payload used to create or update one work experience entry.")
public record UpsertProfileExperienceRequest(
        @Schema(description = "Employer or organization", example = "Acme Corp")
        @NotBlank(message = "company is required")
        @Size(max = 255, message = "company must be at most 255 characters")
        String company,
        @Schema(description = "Role title", example = "Software Engineer")
        @NotBlank(message = "title is required")
        @Size(max = 255, message = "title must be at most 255 characters")
        String title,
        @Schema(description = "Work location", example = "Remote")
        @Size(max = 255, message = "location must be at most 255 characters")
        String location,
        @Schema(description = "Start date of the role", example = "2024-01-01")
        LocalDate startDate,
        @Schema(description = "End date of the role; should be null when `isCurrent` is true", example = "2025-01-01")
        LocalDate endDate,
        @Schema(description = "Whether the role is ongoing", example = "true")
        @NotNull(message = "isCurrent is required")
        Boolean isCurrent,
        @Schema(description = "Description, bullets, or accomplishments for the role", example = "Built internal APIs and improved onboarding automation.")
        @Size(max = 5000, message = "description must be at most 5000 characters")
        String description,
        @Schema(description = "Order within the experiences list", example = "0")
        @NotNull(message = "sortOrder is required")
        @Min(value = 0, message = "sortOrder must be 0 or greater")
        Integer sortOrder
) {
}
