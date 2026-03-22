package com.resume2site.backend.profile.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpsertProfileExperienceRequest(
        @NotBlank(message = "company is required")
        @Size(max = 255, message = "company must be at most 255 characters")
        String company,
        @NotBlank(message = "title is required")
        @Size(max = 255, message = "title must be at most 255 characters")
        String title,
        @Size(max = 255, message = "location must be at most 255 characters")
        String location,
        LocalDate startDate,
        LocalDate endDate,
        @NotNull(message = "isCurrent is required")
        Boolean isCurrent,
        @Size(max = 5000, message = "description must be at most 5000 characters")
        String description,
        @NotNull(message = "sortOrder is required")
        @Min(value = 0, message = "sortOrder must be 0 or greater")
        Integer sortOrder
) {
}
