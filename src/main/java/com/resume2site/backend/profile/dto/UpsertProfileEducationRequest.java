package com.resume2site.backend.profile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpsertProfileEducationRequest(
        @NotBlank(message = "institution is required")
        @Size(max = 255, message = "institution must be at most 255 characters")
        String institution,
        @Size(max = 255, message = "degree must be at most 255 characters")
        String degree,
        @Size(max = 255, message = "fieldOfStudy must be at most 255 characters")
        String fieldOfStudy,
        LocalDate startDate,
        LocalDate endDate,
        @Size(max = 100, message = "grade must be at most 100 characters")
        String grade,
        @Size(max = 5000, message = "description must be at most 5000 characters")
        String description,
        @NotNull(message = "sortOrder is required")
        Integer sortOrder
) {
}
