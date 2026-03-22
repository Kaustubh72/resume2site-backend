package com.resume2site.backend.profile.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpsertProfileProjectRequest(
        @NotBlank(message = "name is required")
        @Size(max = 255, message = "name must be at most 255 characters")
        String name,
        @Size(max = 5000, message = "description must be at most 5000 characters")
        String description,
        @Size(max = 500, message = "projectUrl must be at most 500 characters")
        @Pattern(regexp = "^(https?://).+|^$", message = "projectUrl must start with http:// or https://")
        String projectUrl,
        @Size(max = 500, message = "repositoryUrl must be at most 500 characters")
        @Pattern(regexp = "^(https?://).+|^$", message = "repositoryUrl must start with http:// or https://")
        String repositoryUrl,
        @Size(max = 5000, message = "techStack must be at most 5000 characters")
        String techStack,
        @NotNull(message = "sortOrder is required")
        @Min(value = 0, message = "sortOrder must be 0 or greater")
        Integer sortOrder
) {
}
