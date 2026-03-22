package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

@Schema(name = "PublicProfileResponse", description = "Public-safe portfolio payload returned for a published slug. Internal draft and ownership fields are intentionally omitted.")
public record PublicProfileResponse(
        @Schema(description = "Published public slug", example = "alice-johnson")
        String slug,
        @Schema(description = "Timestamp of the latest publish/republish event", example = "2026-03-22T10:15:30Z")
        Instant publishedAt,
        @Schema(description = "Template metadata needed by the frontend renderer")
        PublicTemplateSelectionResponse template,
        @Schema(description = "Public profile content rendered by the chosen template")
        PublicProfileContentResponse profile
) {
    @Schema(name = "PublicTemplateSelectionResponse", description = "Minimal template metadata required to render a published profile.")
    public record PublicTemplateSelectionResponse(
            @Schema(description = "Template id", example = "2")
            Long id,
            @Schema(description = "Stable template code", example = "modern-stack")
            String code,
            @Schema(description = "Human-readable template name", example = "Modern Stack")
            String name
    ) {
    }

    @Schema(name = "PublicProfileContentResponse", description = "Public fields of the published profile. These are the frontend-renderable values for `/u/{slug}`.")
    public record PublicProfileContentResponse(
            @Schema(description = "Full name displayed on the public portfolio", example = "Alice Johnson")
            String fullName,
            @Schema(description = "Headline displayed on the public portfolio", example = "Software Engineer")
            String headline,
            @Schema(description = "Summary/about text displayed publicly", example = "Backend-focused engineer building reliable Spring services.")
            String summary,
            @Schema(description = "Public email value if the user keeps it in profile data", example = "alice@example.com")
            String email,
            @Schema(description = "Public phone value if the user keeps it in profile data", example = "+1-555-0100")
            String phone,
            @Schema(description = "Public location string", example = "New York, NY")
            String location,
            @Schema(description = "Section visibility/order metadata")
            List<ProfileSectionResponse> sections,
            @Schema(description = "Public links")
            List<ProfileLinkResponse> links,
            @Schema(description = "Public skills")
            List<ProfileSkillResponse> skills,
            @Schema(description = "Public experiences")
            List<ProfileExperienceResponse> experiences,
            @Schema(description = "Public education entries")
            List<ProfileEducationResponse> education,
            @Schema(description = "Public projects")
            List<ProfileProjectResponse> projects
    ) {
    }
}
