package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProfileSummaryResponse", description = "Lightweight draft profile summary returned immediately after parsing a resume.")
public record ProfileSummaryResponse(
        @Schema(description = "Profile id", example = "10")
        Long id,
        @Schema(description = "Anonymous draft ownership token. Required in `X-Draft-Token` while the draft is still unauthenticated.", example = "high-entropy-draft-token")
        String draftToken,
        @Schema(description = "Best-effort parsed full name", example = "Alice Johnson")
        String fullName,
        @Schema(description = "Short headline if available", example = "Software Engineer")
        String headline,
        @Schema(description = "Publication lifecycle state", example = "DRAFT")
        String publicationStatus,
        @Schema(description = "Public slug once published; null for drafts", example = "alice-johnson")
        String slug,
        @Schema(description = "Selected template id when one has been chosen", example = "2")
        Long templateId
) {
}
