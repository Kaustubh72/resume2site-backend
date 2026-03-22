package com.resume2site.backend.profile;

import java.util.List;

public final class ProfileConstants {

    public static final String STATUS_DRAFT = "DRAFT";
    public static final String STATUS_PUBLISHED = "PUBLISHED";
    public static final String PUBLIC_PROFILE_PATH_PREFIX = "/u/";
    public static final String MESSAGE_PUBLISHED_PROFILE_NOT_FOUND = "Published profile not found";
    public static final String MESSAGE_TEMPLATE_NOT_FOUND_OR_INACTIVE = "Template not found or inactive";
    public static final String MESSAGE_PROFILE_MUST_BE_PUBLISHED_BEFORE_SLUG_UPDATE = "Profile must be published before updating slug";
    public static final String MESSAGE_TEMPLATE_REQUIRED_BEFORE_PUBLISH = "templateId is required before publishing";
    public static final String MESSAGE_AUTH_REQUIRED_TO_PUBLISH = "Authentication is required to publish this profile";
    public static final String MESSAGE_AUTHENTICATED_USER_NOT_FOUND = "Authenticated user not found";
    public static final String MESSAGE_PROFILE_ACCESS_DENIED = "You do not have access to this profile";
    public static final String MESSAGE_PROFILE_NOT_FOUND = "Profile not found";
    public static final String MESSAGE_DRAFT_TOKEN_REQUIRED = "A valid draft token is required to access this profile";
    public static final String MESSAGE_UNSUPPORTED_SECTION_KEY_PREFIX = "Unsupported sectionKey: ";
    public static final String MESSAGE_LINK_NOT_FOUND = "Link not found";
    public static final String MESSAGE_SKILL_NOT_FOUND = "Skill not found";
    public static final String MESSAGE_EXPERIENCE_NOT_FOUND = "Experience not found";
    public static final String MESSAGE_EDUCATION_NOT_FOUND = "Education not found";
    public static final String MESSAGE_PROJECT_NOT_FOUND = "Project not found";
    public static final String MESSAGE_DUPLICATE_SECTION_KEY_PREFIX = "Duplicate sectionKey is not allowed: ";
    public static final String MESSAGE_DUPLICATE_SORT_ORDER_PREFIX = "Duplicate sortOrder is not allowed: ";
    public static final String MESSAGE_END_DATE_MUST_BE_NULL_WHEN_CURRENT = "endDate must be null when isCurrent is true";
    public static final String MESSAGE_END_DATE_ORDER_PREFIX = "endDate must be on or after startDate for ";
    public static final String MESSAGE_UNSUPPORTED_CHILD_ENTITY = "Unsupported profile child entity";
    public static final String SECTION_EDUCATION = "education";
    public static final String SECTION_EXPERIENCE = "experience";
    public static final List<DefaultSectionDefinition> DEFAULT_SECTIONS = List.of(
            new DefaultSectionDefinition("summary", "Summary", 0),
            new DefaultSectionDefinition("links", "Links", 1),
            new DefaultSectionDefinition("skills", "Skills", 2),
            new DefaultSectionDefinition("experiences", "Experience", 3),
            new DefaultSectionDefinition("projects", "Projects", 4),
            new DefaultSectionDefinition("education", "Education", 5)
    );

    private ProfileConstants() {
    }

    public record DefaultSectionDefinition(String sectionKey, String displayName, Integer sortOrder) {
    }
}
