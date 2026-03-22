package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProfileSkillResponse", description = "One skill or technology listed on the profile.")
public record ProfileSkillResponse(
        @Schema(description = "Skill id", example = "201")
        Long id,
        @Schema(description = "Skill name", example = "Spring Boot")
        String name,
        @Schema(description = "Optional grouping/category label", example = "Frameworks")
        String category,
        @Schema(description = "Order within the skills collection", example = "0")
        Integer sortOrder
) {
}
