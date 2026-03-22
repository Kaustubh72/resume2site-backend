package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "UpsertProfileSkillRequest", description = "Payload used to create or update one skill entry.")
public record UpsertProfileSkillRequest(
        @Schema(description = "Skill name", example = "Spring Boot")
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,
        @Schema(description = "Optional category/group label", example = "Frameworks")
        @Size(max = 100, message = "category must be at most 100 characters")
        String category,
        @Schema(description = "Order within the skills list", example = "0")
        @NotNull(message = "sortOrder is required")
        @Min(value = 0, message = "sortOrder must be 0 or greater")
        Integer sortOrder
) {
}
