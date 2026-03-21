package com.resume2site.backend.profile.dto;

import java.util.List;

public record ProfileDetailResponse(
        Long id,
        String draftToken,
        String fullName,
        String headline,
        String summary,
        String email,
        String phone,
        String location,
        String publicationStatus,
        String slug,
        Long templateId,
        List<ProfileSectionResponse> sections,
        List<ProfileLinkResponse> links,
        List<ProfileSkillResponse> skills,
        List<ProfileExperienceResponse> experiences,
        List<ProfileEducationResponse> education,
        List<ProfileProjectResponse> projects
) {
}
