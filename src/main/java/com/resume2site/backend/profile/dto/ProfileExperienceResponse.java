package com.resume2site.backend.profile.dto;

import java.time.LocalDate;

public record ProfileExperienceResponse(
        Long id,
        String company,
        String title,
        String location,
        LocalDate startDate,
        LocalDate endDate,
        boolean isCurrent,
        String description,
        Integer sortOrder
) {
}
