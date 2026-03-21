package com.resume2site.backend.profile.dto;

public record ProfileLinkResponse(
        Long id,
        String label,
        String url,
        Integer sortOrder
) {
}
