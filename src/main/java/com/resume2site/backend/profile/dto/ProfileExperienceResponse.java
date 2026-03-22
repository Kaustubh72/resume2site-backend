package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name = "ProfileExperienceResponse", description = "One work experience item on the editable or public profile.")
public record ProfileExperienceResponse(
        @Schema(description = "Experience id", example = "301")
        Long id,
        @Schema(description = "Employer or organization name", example = "Acme Corp")
        String company,
        @Schema(description = "Job title or role name", example = "Software Engineer")
        String title,
        @Schema(description = "Work location if provided", example = "Remote")
        String location,
        @Schema(description = "Role start date", example = "2024-01-01")
        LocalDate startDate,
        @Schema(description = "Role end date. Null when `isCurrent` is true.", example = "2025-01-01")
        LocalDate endDate,
        @Schema(description = "Whether this role is ongoing", example = "true")
        boolean isCurrent,
        @Schema(description = "Free-form description or bullet summary for the experience", example = "Built internal APIs and improved onboarding flows.")
        String description,
        @Schema(description = "Order within the experiences collection", example = "0")
        Integer sortOrder
) {
}
