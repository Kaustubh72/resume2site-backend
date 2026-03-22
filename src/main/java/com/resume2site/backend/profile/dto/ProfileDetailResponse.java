package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "ProfileDetailResponse", description = "Full editable profile payload returned to the profile editor. For anonymous drafts, `draftToken` must be stored client-side and sent back in `X-Draft-Token`.")
public record ProfileDetailResponse(
        @Schema(description = "Profile id", example = "10")
        Long id,
        @Schema(description = "Anonymous draft token used for draft ownership until the profile is attached to an authenticated user", example = "high-entropy-draft-token")
        String draftToken,
        @Schema(description = "Profile full name", example = "Alice Johnson")
        String fullName,
        @Schema(description = "Profile headline/subtitle", example = "Software Engineer")
        String headline,
        @Schema(description = "Professional summary/about text", example = "Backend-focused engineer building Spring Boot systems.")
        String summary,
        @Schema(description = "Contact email shown in profile data", example = "alice@example.com")
        String email,
        @Schema(description = "Phone number shown in profile data", example = "+1-555-0100")
        String phone,
        @Schema(description = "Location string shown in profile data", example = "New York, NY")
        String location,
        @Schema(description = "Profile publication state", example = "DRAFT")
        String publicationStatus,
        @Schema(description = "Public slug once published", example = "alice-johnson")
        String slug,
        @Schema(description = "Selected template id", example = "2")
        Long templateId,
        @Schema(description = "Configured logical sections for visibility and order")
        List<ProfileSectionResponse> sections,
        @Schema(description = "Profile links collection")
        List<ProfileLinkResponse> links,
        @Schema(description = "Profile skills collection")
        List<ProfileSkillResponse> skills,
        @Schema(description = "Profile experience collection")
        List<ProfileExperienceResponse> experiences,
        @Schema(description = "Profile education collection")
        List<ProfileEducationResponse> education,
        @Schema(description = "Profile projects collection")
        List<ProfileProjectResponse> projects
) {
}
