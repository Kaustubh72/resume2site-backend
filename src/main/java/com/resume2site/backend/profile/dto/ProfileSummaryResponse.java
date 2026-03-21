package com.resume2site.backend.profile.dto;

public record ProfileSummaryResponse(
        Long id,
        String draftToken,
        String fullName,
        String headline,
        String publicationStatus,
        String slug,
        Long templateId
) {
}
