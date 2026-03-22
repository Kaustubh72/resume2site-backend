package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Schema(name = "UpsertProfileEducationRequest", description = "Payload used to create or update one education entry.")
public record UpsertProfileEducationRequest(
        @Schema(description = "Institution name", example = "State University")
        @NotBlank(message = "institution is required")
        @Size(max = 255, message = "institution must be at most 255 characters")
        String institution,
        @Schema(description = "Degree/program name", example = "B.S. Computer Science")
        @Size(max = 255, message = "degree must be at most 255 characters")
        String degree,
        @Schema(description = "Field of study", example = "Computer Science")
        @Size(max = 255, message = "fieldOfStudy must be at most 255 characters")
        String fieldOfStudy,
        @Schema(description = "Start date", example = "2020-08-01")
        LocalDate startDate,
        @Schema(description = "End date", example = "2024-05-01")
        LocalDate endDate,
        @Schema(description = "Optional grade/GPA/honors note", example = "3.8 GPA")
        @Size(max = 100, message = "grade must be at most 100 characters")
        String grade,
        @Schema(description = "Additional education details", example = "Relevant coursework in distributed systems.")
        @Size(max = 5000, message = "description must be at most 5000 characters")
        String description,
        @Schema(description = "Order within the education list", example = "0")
        @NotNull(message = "sortOrder is required")
        @Min(value = 0, message = "sortOrder must be 0 or greater")
        Integer sortOrder
) {
}
