package com.resume2site.backend.profile.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpsertProfileSkillRequest(
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,
        @Size(max = 100, message = "category must be at most 100 characters")
        String category,
        @NotNull(message = "sortOrder is required")
        @Min(value = 0, message = "sortOrder must be 0 or greater")
        Integer sortOrder
) {
}
