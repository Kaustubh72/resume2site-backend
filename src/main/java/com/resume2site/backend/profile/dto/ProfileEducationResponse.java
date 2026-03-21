package com.resume2site.backend.profile.dto;

import java.time.LocalDate;

public record ProfileEducationResponse(
        Long id,
        String institution,
        String degree,
        String fieldOfStudy,
        LocalDate startDate,
        LocalDate endDate,
        String grade,
        String description,
        Integer sortOrder
) {
}
