package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "UpsertProfileProjectRequest", description = "Payload used to create or update one project entry.")
public record UpsertProfileProjectRequest(
        @Schema(description = "Project name", example = "Resume2Site")
        @NotBlank(message = "name is required")
        @Size(max = 255, message = "name must be at most 255 characters")
        String name,
        @Schema(description = "Project description", example = "Resume-first portfolio publishing platform")
        @Size(max = 5000, message = "description must be at most 5000 characters")
        String description,
        @Schema(description = "Optional live project/demo URL", example = "https://resume2site.dev")
        @Size(max = 500, message = "projectUrl must be at most 500 characters")
        @Pattern(regexp = "^(https?://).+|^$", message = "projectUrl must start with http:// or https://")
        String projectUrl,
        @Schema(description = "Optional source repository URL", example = "https://github.com/alice/resume2site")
        @Size(max = 500, message = "repositoryUrl must be at most 500 characters")
        @Pattern(regexp = "^(https?://).+|^$", message = "repositoryUrl must start with http:// or https://")
        String repositoryUrl,
        @Schema(description = "Free-form technologies used in the project", example = "Java, Spring Boot, React, PostgreSQL")
        @Size(max = 5000, message = "techStack must be at most 5000 characters")
        String techStack,
        @Schema(description = "Order within the projects list", example = "0")
        @NotNull(message = "sortOrder is required")
        @Min(value = 0, message = "sortOrder must be 0 or greater")
        Integer sortOrder
) {
}
