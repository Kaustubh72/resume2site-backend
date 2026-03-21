package com.resume2site.backend.profile.dto;

public record PublishProfileResponse(
        Long profileId,
        String slug,
        String publicationStatus,
        Long templateId,
        String publicUrl
) {
}
