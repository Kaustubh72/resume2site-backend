package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name = "ProfileEducationResponse", description = "One education entry on the editable or public profile.")
public record ProfileEducationResponse(
        @Schema(description = "Education id", example = "401")
        Long id,
        @Schema(description = "Institution name", example = "State University")
        String institution,
        @Schema(description = "Degree or program name", example = "B.S. Computer Science")
        String degree,
        @Schema(description = "Field of study if captured separately", example = "Computer Science")
        String fieldOfStudy,
        @Schema(description = "Education start date", example = "2020-08-01")
        LocalDate startDate,
        @Schema(description = "Education end date", example = "2024-05-01")
        LocalDate endDate,
        @Schema(description = "Optional grade/GPA/honors note", example = "3.8 GPA")
        String grade,
        @Schema(description = "Additional notes about the education entry", example = "Relevant coursework in distributed systems and databases.")
        String description,
        @Schema(description = "Order within the education collection", example = "0")
        Integer sortOrder
) {
}
