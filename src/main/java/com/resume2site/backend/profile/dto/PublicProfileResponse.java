package com.resume2site.backend.profile.dto;

import java.time.Instant;
import java.util.List;

public record PublicProfileResponse(
        String slug,
        Instant publishedAt,
        PublicTemplateSelectionResponse template,
        PublicProfileContentResponse profile
) {
    public record PublicTemplateSelectionResponse(
            Long id,
            String code,
            String name
    ) {
    }

    public record PublicProfileContentResponse(
            String fullName,
            String headline,
            String summary,
            String email,
            String phone,
            String location,
            List<ProfileSectionResponse> sections,
            List<ProfileLinkResponse> links,
            List<ProfileSkillResponse> skills,
            List<ProfileExperienceResponse> experiences,
            List<ProfileEducationResponse> education,
            List<ProfileProjectResponse> projects
    ) {
    }
}
