package com.resume2site.backend.profile.dto;

public record ProfileProjectResponse(
        Long id,
        String name,
        String description,
        String projectUrl,
        String repositoryUrl,
        String techStack,
        Integer sortOrder
) {
}
