package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(name = "UpdateProfileSectionsRequest", description = "Bulk update payload for profile section visibility, labels, and ordering.")
public record UpdateProfileSectionsRequest(
        @Schema(description = "Complete list of editable sections with their desired visibility and order")
        @NotEmpty(message = "sections are required")
        List<@Valid SectionItem> sections
) {
    @Schema(name = "ProfileSectionItem", description = "One editable section configuration in the profile editor.")
    public record SectionItem(
            @Schema(description = "Stable section key expected by the backend and frontend", example = "summary")
            @NotNull(message = "sectionKey is required")
            @Size(min = 1, max = 100, message = "sectionKey must be between 1 and 100 characters")
            String sectionKey,
            @Schema(description = "User-facing label for the section", example = "About")
            @NotNull(message = "displayName is required")
            @Size(min = 1, max = 100, message = "displayName must be between 1 and 100 characters")
            String displayName,
            @Schema(description = "Whether the section should be shown publicly", example = "true")
            @NotNull(message = "visible is required")
            Boolean visible,
            @Schema(description = "Desired sort order among sections", example = "0")
            @NotNull(message = "sortOrder is required")
            @Min(value = 0, message = "sortOrder must be 0 or greater")
            Integer sortOrder
    ) {
    }
}
