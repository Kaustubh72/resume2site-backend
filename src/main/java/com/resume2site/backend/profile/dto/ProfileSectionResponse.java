package com.resume2site.backend.profile.dto;

public record ProfileSectionResponse(
        String sectionKey,
        String displayName,
        boolean visible,
        Integer sortOrder
) {
}
