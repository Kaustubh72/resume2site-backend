package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProfileProjectResponse", description = "One project entry shown in the portfolio.")
public record ProfileProjectResponse(
        @Schema(description = "Project id", example = "501")
        Long id,
        @Schema(description = "Project name", example = "Resume2Site")
        String name,
        @Schema(description = "Short project description", example = "Resume-first portfolio publishing platform")
        String description,
        @Schema(description = "Optional live demo/project URL", example = "https://resume2site.dev")
        String projectUrl,
        @Schema(description = "Optional source repository URL", example = "https://github.com/alice/resume2site")
        String repositoryUrl,
        @Schema(description = "Free-form technologies used in the project", example = "Java, Spring Boot, React, PostgreSQL")
        String techStack,
        @Schema(description = "Order within the projects collection", example = "0")
        Integer sortOrder
) {
}
