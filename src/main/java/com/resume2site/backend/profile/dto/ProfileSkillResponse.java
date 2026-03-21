package com.resume2site.backend.profile.dto;

public record ProfileSkillResponse(
        Long id,
        String name,
        String category,
        Integer sortOrder
) {
}
