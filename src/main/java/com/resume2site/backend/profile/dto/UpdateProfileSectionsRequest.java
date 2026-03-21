package com.resume2site.backend.profile.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdateProfileSectionsRequest(
        @NotEmpty(message = "sections are required")
        List<@Valid SectionItem> sections
) {
    public record SectionItem(
            @NotNull(message = "sectionKey is required")
            @Size(max = 100, message = "sectionKey must be at most 100 characters")
            String sectionKey,
            @NotNull(message = "displayName is required")
            @Size(max = 100, message = "displayName must be at most 100 characters")
            String displayName,
            @NotNull(message = "visible is required")
            Boolean visible,
            @NotNull(message = "sortOrder is required")
            Integer sortOrder
    ) {
    }
}
