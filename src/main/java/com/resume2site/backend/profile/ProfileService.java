package com.resume2site.backend.profile;

import com.resume2site.backend.common.exception.BadRequestException;
import com.resume2site.backend.common.exception.ResourceNotFoundException;
import com.resume2site.backend.common.exception.UnauthorizedException;
import com.resume2site.backend.profile.domain.Profile;
import com.resume2site.backend.profile.domain.ProfileEducation;
import com.resume2site.backend.profile.domain.ProfileExperience;
import com.resume2site.backend.profile.domain.ProfileLink;
import com.resume2site.backend.profile.domain.ProfileProject;
import com.resume2site.backend.profile.domain.ProfileSection;
import com.resume2site.backend.profile.domain.ProfileSkill;
import com.resume2site.backend.profile.dto.*;
import com.resume2site.backend.profile.repository.*;
import com.resume2site.backend.security.jwt.AuthenticatedUser;
import com.resume2site.backend.template.domain.Template;
import com.resume2site.backend.template.repository.TemplateRepository;
import com.resume2site.backend.user.domain.User;
import com.resume2site.backend.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.time.Instant;
import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private static final List<DefaultSection> DEFAULT_SECTIONS = List.of(
            new DefaultSection("summary", "Summary", 0),
            new DefaultSection("links", "Links", 1),
            new DefaultSection("skills", "Skills", 2),
            new DefaultSection("experiences", "Experience", 3),
            new DefaultSection("projects", "Projects", 4),
            new DefaultSection("education", "Education", 5)
    );

    private final ProfileRepository profileRepository;
    private static final String STATUS_DRAFT = "DRAFT";
    private static final String STATUS_PUBLISHED = "PUBLISHED";
    private static final String PUBLIC_PROFILE_PATH_PREFIX = "/u/";

    private final TemplateRepository templateRepository;
    private final ProfileSectionRepository profileSectionRepository;
    private final ProfileLinkRepository profileLinkRepository;
    private final ProfileSkillRepository profileSkillRepository;
    private final ProfileExperienceRepository profileExperienceRepository;
    private final ProfileEducationRepository profileEducationRepository;
    private final ProfileProjectRepository profileProjectRepository;
    private final UserRepository userRepository;
    private final SlugService slugService;

    public ProfileService(ProfileRepository profileRepository,
                          TemplateRepository templateRepository,
                          ProfileSectionRepository profileSectionRepository,
                          ProfileLinkRepository profileLinkRepository,
                          ProfileSkillRepository profileSkillRepository,
                          ProfileExperienceRepository profileExperienceRepository,
                          ProfileEducationRepository profileEducationRepository,
                          ProfileProjectRepository profileProjectRepository,
                          UserRepository userRepository,
                          SlugService slugService) {
        this.profileRepository = profileRepository;
        this.templateRepository = templateRepository;
        this.profileSectionRepository = profileSectionRepository;
        this.profileLinkRepository = profileLinkRepository;
        this.profileSkillRepository = profileSkillRepository;
        this.profileExperienceRepository = profileExperienceRepository;
        this.profileEducationRepository = profileEducationRepository;
        this.profileProjectRepository = profileProjectRepository;
        this.userRepository = userRepository;
        this.slugService = slugService;
    }


    @Transactional(readOnly = true)
    public PublicProfileResponse getPublicProfile(String slug) {
        String normalizedSlug = trimToNull(slug);
        if (normalizedSlug == null) {
            throw new ResourceNotFoundException("Published profile not found");
        }

        Profile profile = profileRepository.findBySlugIgnoreCaseAndPublicationStatus(normalizedSlug, STATUS_PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Published profile not found"));

        return new PublicProfileResponse(
                profile.getSlug(),
                profile.getPublishedAt(),
                new PublicProfileResponse.PublicTemplateSelectionResponse(
                        profile.getTemplate().getId(),
                        profile.getTemplate().getCode(),
                        profile.getTemplate().getName()
                ),
                new PublicProfileResponse.PublicProfileContentResponse(
                        profile.getFullName(),
                        profile.getHeadline(),
                        profile.getProfessionalSummary(),
                        profile.getEmail(),
                        profile.getPhone(),
                        profile.getLocation(),
                        profileSectionRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId()).stream()
                                .map(this::toSectionResponse)
                                .toList(),
                        profileLinkRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId()).stream()
                                .map(this::toLinkResponse)
                                .toList(),
                        profileSkillRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId()).stream()
                                .map(this::toSkillResponse)
                                .toList(),
                        profileExperienceRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId()).stream()
                                .map(this::toExperienceResponse)
                                .toList(),
                        profileEducationRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId()).stream()
                                .map(this::toEducationResponse)
                                .toList(),
                        profileProjectRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId()).stream()
                                .map(this::toProjectResponse)
                                .toList()
                )
        );
    }

    @Transactional
    public ProfileDetailResponse getProfile(Long profileId, String draftToken, AuthenticatedUser authenticatedUser) {
        Profile profile = loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ensureDefaultSections(profile);
        return toProfileDetail(profile);
    }

    @Transactional
    public ProfileDetailResponse updateProfile(Long profileId,
                                               UpdateProfileRequest request,
                                               String draftToken,
                                               AuthenticatedUser authenticatedUser) {
        Profile profile = loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        Template template = null;
        if (request.templateId() != null) {
            template = templateRepository.findById(request.templateId())
                    .filter(Template::isActive)
                    .orElseThrow(() -> new BadRequestException("Template not found or inactive"));
        }

        profile.setFullName(trimToNull(request.fullName()));
        profile.setHeadline(trimToNull(request.headline()));
        profile.setProfessionalSummary(trimToNull(request.summary()));
        profile.setEmail(normalizeEmail(request.email()));
        profile.setPhone(trimToNull(request.phone()));
        profile.setLocation(trimToNull(request.location()));
        profile.setTemplate(template);

        ensureDefaultSections(profile);
        return toProfileDetail(profileRepository.save(profile));
    }

    @Transactional
    public List<ProfileSectionResponse> updateSections(Long profileId,
                                                       UpdateProfileSectionsRequest request,
                                                       String draftToken,
                                                       AuthenticatedUser authenticatedUser) {
        Profile profile = loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ensureDefaultSections(profile);
        validateSections(request.sections());

        Map<String, ProfileSection> sectionsByKey = new LinkedHashMap<>();
        for (ProfileSection section : profileSectionRepository.findAllByProfileIdOrderBySortOrderAsc(profileId)) {
            sectionsByKey.put(section.getSectionKey(), section);
        }

        for (UpdateProfileSectionsRequest.SectionItem item : request.sections()) {
            ProfileSection section = sectionsByKey.get(item.sectionKey().trim().toLowerCase(Locale.ROOT));
            if (section == null) {
                throw new BadRequestException("Unsupported sectionKey: " + item.sectionKey());
            }
            section.setDisplayName(item.displayName().trim());
            section.setVisible(item.visible());
            section.setSortOrder(item.sortOrder());
        }

        return profileSectionRepository.saveAll(sectionsByKey.values()).stream()
                .sorted((left, right) -> Integer.compare(left.getSortOrder(), right.getSortOrder()))
                .map(this::toSectionResponse)
                .toList();
    }

    @Transactional
    public ProfileLinkResponse createLink(Long profileId, UpsertProfileLinkRequest request, String draftToken, AuthenticatedUser authenticatedUser) {
        Profile profile = loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileLink link = new ProfileLink();
        link.setProfile(profile);
        applyLink(link, request);
        return toLinkResponse(profileLinkRepository.save(link));
    }

    @Transactional
    public ProfileLinkResponse updateLink(Long profileId, Long linkId, UpsertProfileLinkRequest request, String draftToken, AuthenticatedUser authenticatedUser) {
        loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileLink link = requireOwned(profileLinkRepository.findById(linkId).orElseThrow(() -> new ResourceNotFoundException("Link not found")), profileId, "Link not found");
        applyLink(link, request);
        return toLinkResponse(profileLinkRepository.save(link));
    }

    @Transactional
    public void deleteLink(Long profileId, Long linkId, String draftToken, AuthenticatedUser authenticatedUser) {
        loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileLink link = requireOwned(profileLinkRepository.findById(linkId).orElseThrow(() -> new ResourceNotFoundException("Link not found")), profileId, "Link not found");
        profileLinkRepository.delete(link);
    }

    @Transactional
    public ProfileSkillResponse createSkill(Long profileId, UpsertProfileSkillRequest request, String draftToken, AuthenticatedUser authenticatedUser) {
        Profile profile = loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileSkill skill = new ProfileSkill();
        skill.setProfile(profile);
        applySkill(skill, request);
        return toSkillResponse(profileSkillRepository.save(skill));
    }

    @Transactional
    public ProfileSkillResponse updateSkill(Long profileId, Long skillId, UpsertProfileSkillRequest request, String draftToken, AuthenticatedUser authenticatedUser) {
        loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileSkill skill = requireOwned(profileSkillRepository.findById(skillId).orElseThrow(() -> new ResourceNotFoundException("Skill not found")), profileId, "Skill not found");
        applySkill(skill, request);
        return toSkillResponse(profileSkillRepository.save(skill));
    }

    @Transactional
    public void deleteSkill(Long profileId, Long skillId, String draftToken, AuthenticatedUser authenticatedUser) {
        loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileSkill skill = requireOwned(profileSkillRepository.findById(skillId).orElseThrow(() -> new ResourceNotFoundException("Skill not found")), profileId, "Skill not found");
        profileSkillRepository.delete(skill);
    }

    @Transactional
    public ProfileExperienceResponse createExperience(Long profileId, UpsertProfileExperienceRequest request, String draftToken, AuthenticatedUser authenticatedUser) {
        validateExperienceDates(request.startDate(), request.endDate(), request.isCurrent());
        Profile profile = loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileExperience experience = new ProfileExperience();
        experience.setProfile(profile);
        applyExperience(experience, request);
        return toExperienceResponse(profileExperienceRepository.save(experience));
    }

    @Transactional
    public ProfileExperienceResponse updateExperience(Long profileId, Long experienceId, UpsertProfileExperienceRequest request, String draftToken, AuthenticatedUser authenticatedUser) {
        validateExperienceDates(request.startDate(), request.endDate(), request.isCurrent());
        loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileExperience experience = requireOwned(profileExperienceRepository.findById(experienceId).orElseThrow(() -> new ResourceNotFoundException("Experience not found")), profileId, "Experience not found");
        applyExperience(experience, request);
        return toExperienceResponse(profileExperienceRepository.save(experience));
    }

    @Transactional
    public void deleteExperience(Long profileId, Long experienceId, String draftToken, AuthenticatedUser authenticatedUser) {
        loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileExperience experience = requireOwned(profileExperienceRepository.findById(experienceId).orElseThrow(() -> new ResourceNotFoundException("Experience not found")), profileId, "Experience not found");
        profileExperienceRepository.delete(experience);
    }

    @Transactional
    public ProfileEducationResponse createEducation(Long profileId, UpsertProfileEducationRequest request, String draftToken, AuthenticatedUser authenticatedUser) {
        validateChronologicalDates(request.startDate(), request.endDate(), "education");
        Profile profile = loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileEducation education = new ProfileEducation();
        education.setProfile(profile);
        applyEducation(education, request);
        return toEducationResponse(profileEducationRepository.save(education));
    }

    @Transactional
    public ProfileEducationResponse updateEducation(Long profileId, Long educationId, UpsertProfileEducationRequest request, String draftToken, AuthenticatedUser authenticatedUser) {
        validateChronologicalDates(request.startDate(), request.endDate(), "education");
        loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileEducation education = requireOwned(profileEducationRepository.findById(educationId).orElseThrow(() -> new ResourceNotFoundException("Education not found")), profileId, "Education not found");
        applyEducation(education, request);
        return toEducationResponse(profileEducationRepository.save(education));
    }

    @Transactional
    public void deleteEducation(Long profileId, Long educationId, String draftToken, AuthenticatedUser authenticatedUser) {
        loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileEducation education = requireOwned(profileEducationRepository.findById(educationId).orElseThrow(() -> new ResourceNotFoundException("Education not found")), profileId, "Education not found");
        profileEducationRepository.delete(education);
    }

    @Transactional
    public ProfileProjectResponse createProject(Long profileId, UpsertProfileProjectRequest request, String draftToken, AuthenticatedUser authenticatedUser) {
        Profile profile = loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileProject project = new ProfileProject();
        project.setProfile(profile);
        applyProject(project, request);
        return toProjectResponse(profileProjectRepository.save(project));
    }

    @Transactional
    public ProfileProjectResponse updateProject(Long profileId, Long projectId, UpsertProfileProjectRequest request, String draftToken, AuthenticatedUser authenticatedUser) {
        loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileProject project = requireOwned(profileProjectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found")), profileId, "Project not found");
        applyProject(project, request);
        return toProjectResponse(profileProjectRepository.save(project));
    }

    @Transactional
    public void deleteProject(Long profileId, Long projectId, String draftToken, AuthenticatedUser authenticatedUser) {
        loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
        ProfileProject project = requireOwned(profileProjectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found")), profileId, "Project not found");
        profileProjectRepository.delete(project);
    }

    @Transactional
    public PublishProfileResponse publishProfile(Long profileId,
                                                 PublishProfileRequest request,
                                                 String draftToken,
                                                 AuthenticatedUser authenticatedUser) {
        Profile profile = requireAuthenticatedOwnedProfile(profileId, draftToken, authenticatedUser);
        attachProfileToAuthenticatedUser(profile, authenticatedUser);
        return publish(profile, request.slug());
    }

    @Transactional
    public PublishProfileResponse republishProfile(Long profileId,
                                                   PublishProfileRequest request,
                                                   String draftToken,
                                                   AuthenticatedUser authenticatedUser) {
        Profile profile = requireAuthenticatedOwnedProfile(profileId, draftToken, authenticatedUser);
        return publish(profile, request.slug());
    }

    @Transactional
    public PublishProfileResponse updateSlug(Long profileId,
                                             UpdateProfileSlugRequest request,
                                             AuthenticatedUser authenticatedUser) {
        Profile profile = requireAuthenticatedOwnedProfile(profileId, null, authenticatedUser);
        if (!STATUS_PUBLISHED.equalsIgnoreCase(profile.getPublicationStatus())) {
            throw new BadRequestException("Profile must be published before updating slug");
        }

        String slug = slugService.requireUsableSlug(request.slug(), profile.getId());
        profile.setSlug(slug);
        Profile savedProfile = profileRepository.save(profile);
        return toPublishResponse(savedProfile);
    }

    @Transactional
    public void ensureDefaultSections(Profile profile) {
        List<ProfileSection> existing = profileSectionRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId());
        if (!existing.isEmpty()) {
            return;
        }
        List<ProfileSection> sections = new ArrayList<>();
        for (DefaultSection defaultSection : DEFAULT_SECTIONS) {
            ProfileSection section = new ProfileSection();
            section.setProfile(profile);
            section.setSectionKey(defaultSection.sectionKey());
            section.setDisplayName(defaultSection.displayName());
            section.setVisible(true);
            section.setSortOrder(defaultSection.sortOrder());
            sections.add(section);
        }
        profileSectionRepository.saveAll(sections);
    }

    private PublishProfileResponse publish(Profile profile, String requestedSlug) {
        if (profile.getTemplate() == null) {
            throw new BadRequestException("templateId is required before publishing");
        }

        String slug = slugService.requireUsableSlug(requestedSlug, profile.getId());
        profile.setSlug(slug);
        profile.setPublicationStatus(STATUS_PUBLISHED);
        profile.setPublishedAt(Instant.now());
        Profile savedProfile = profileRepository.save(profile);
        return toPublishResponse(savedProfile);
    }

    private Profile requireAuthenticatedOwnedProfile(Long profileId, String draftToken, AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            throw new UnauthorizedException("Authentication is required to publish this profile");
        }
        return loadAuthorizedProfile(profileId, draftToken, authenticatedUser);
    }

    private void attachProfileToAuthenticatedUser(Profile profile, AuthenticatedUser authenticatedUser) {
        User user = userRepository.findById(authenticatedUser.userId())
                .orElseThrow(() -> new UnauthorizedException("Authenticated user not found"));
        if (profile.getUser() != null && !Objects.equals(profile.getUser().getId(), user.getId())) {
            throw new UnauthorizedException("You do not have access to this profile");
        }
        if (profile.getResumeUpload() != null && profile.getResumeUpload().getUser() == null) {
            profile.getResumeUpload().setUser(user);
        }
        profile.setUser(user);
    }

    private PublishProfileResponse toPublishResponse(Profile profile) {
        return new PublishProfileResponse(
                profile.getId(),
                profile.getSlug(),
                profile.getPublicationStatus(),
                profile.getTemplate() != null ? profile.getTemplate().getId() : null,
                PUBLIC_PROFILE_PATH_PREFIX + profile.getSlug()
        );
    }

    private void applyLink(ProfileLink link, UpsertProfileLinkRequest request) {
        link.setLabel(request.label().trim());
        link.setUrl(request.url().trim());
        link.setSortOrder(request.sortOrder());
    }

    private void applySkill(ProfileSkill skill, UpsertProfileSkillRequest request) {
        skill.setName(request.name().trim());
        skill.setCategory(trimToNull(request.category()));
        skill.setSortOrder(request.sortOrder());
    }

    private void applyExperience(ProfileExperience experience, UpsertProfileExperienceRequest request) {
        experience.setCompany(request.company().trim());
        experience.setTitle(request.title().trim());
        experience.setLocation(trimToNull(request.location()));
        experience.setStartDate(request.startDate());
        experience.setEndDate(request.isCurrent() ? null : request.endDate());
        experience.setCurrent(request.isCurrent());
        experience.setDescription(trimToNull(request.description()));
        experience.setSortOrder(request.sortOrder());
    }

    private void applyEducation(ProfileEducation education, UpsertProfileEducationRequest request) {
        education.setInstitution(request.institution().trim());
        education.setDegree(trimToNull(request.degree()));
        education.setFieldOfStudy(trimToNull(request.fieldOfStudy()));
        education.setStartDate(request.startDate());
        education.setEndDate(request.endDate());
        education.setGrade(trimToNull(request.grade()));
        education.setDescription(trimToNull(request.description()));
        education.setSortOrder(request.sortOrder());
    }

    private void applyProject(ProfileProject project, UpsertProfileProjectRequest request) {
        project.setName(request.name().trim());
        project.setDescription(trimToNull(request.description()));
        project.setProjectUrl(trimToNull(request.projectUrl()));
        project.setRepositoryUrl(trimToNull(request.repositoryUrl()));
        project.setTechStack(trimToNull(request.techStack()));
        project.setSortOrder(request.sortOrder());
    }

    private Profile loadAuthorizedProfile(Long profileId, String draftToken, AuthenticatedUser authenticatedUser) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        if (profile.getUser() != null) {
            if (authenticatedUser == null || !Objects.equals(profile.getUser().getId(), authenticatedUser.userId())) {
                throw new UnauthorizedException("You do not have access to this profile");
            }
            return profile;
        }

        if (draftToken == null || draftToken.isBlank() || !draftToken.equals(profile.getDraftToken())) {
            throw new UnauthorizedException("A valid draft token is required to access this profile");
        }
        return profile;
    }

    private void validateSections(List<UpdateProfileSectionsRequest.SectionItem> sections) {
        Map<String, Integer> seenKeys = new LinkedHashMap<>();
        Map<Integer, String> seenSortOrders = new LinkedHashMap<>();
        for (UpdateProfileSectionsRequest.SectionItem section : sections) {
            String normalizedKey = section.sectionKey().trim().toLowerCase(Locale.ROOT);
            if (seenKeys.put(normalizedKey, 1) != null) {
                throw new BadRequestException("Duplicate sectionKey is not allowed: " + normalizedKey);
            }
            if (seenSortOrders.put(section.sortOrder(), normalizedKey) != null) {
                throw new BadRequestException("Duplicate sortOrder is not allowed: " + section.sortOrder());
            }
        }
    }

    private void validateExperienceDates(java.time.LocalDate startDate, java.time.LocalDate endDate, Boolean isCurrent) {
        if (Boolean.TRUE.equals(isCurrent) && endDate != null) {
            throw new BadRequestException("endDate must be null when isCurrent is true");
        }
        if (!Boolean.TRUE.equals(isCurrent)) {
            validateChronologicalDates(startDate, endDate, "experience");
        }
    }

    private void validateChronologicalDates(java.time.LocalDate startDate, java.time.LocalDate endDate, String section) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new BadRequestException("endDate must be on or after startDate for " + section);
        }
    }

    private String normalizeEmail(String email) {
        String trimmed = trimToNull(email);
        return trimmed == null ? null : trimmed.toLowerCase(Locale.ROOT);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private ProfileDetailResponse toProfileDetail(Profile profile) {
        List<ProfileSectionResponse> sections = profileSectionRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId())
                .stream()
                .map(this::toSectionResponse)
                .toList();
        List<ProfileLinkResponse> links = profileLinkRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId()).stream()
                .map(this::toLinkResponse)
                .toList();
        List<ProfileSkillResponse> skills = profileSkillRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId()).stream()
                .map(this::toSkillResponse)
                .toList();
        List<ProfileExperienceResponse> experiences = profileExperienceRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId()).stream()
                .map(this::toExperienceResponse)
                .toList();
        List<ProfileEducationResponse> education = profileEducationRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId()).stream()
                .map(this::toEducationResponse)
                .toList();
        List<ProfileProjectResponse> projects = profileProjectRepository.findAllByProfileIdOrderBySortOrderAsc(profile.getId()).stream()
                .map(this::toProjectResponse)
                .toList();

        return new ProfileDetailResponse(
                profile.getId(),
                profile.getDraftToken(),
                profile.getFullName(),
                profile.getHeadline(),
                profile.getProfessionalSummary(),
                profile.getEmail(),
                profile.getPhone(),
                profile.getLocation(),
                profile.getPublicationStatus(),
                profile.getSlug(),
                profile.getTemplate() != null ? profile.getTemplate().getId() : null,
                sections,
                links,
                skills,
                experiences,
                education,
                projects
        );
    }

    private ProfileSectionResponse toSectionResponse(ProfileSection section) {
        return new ProfileSectionResponse(section.getSectionKey(), section.getDisplayName(), section.isVisible(), section.getSortOrder());
    }

    private ProfileLinkResponse toLinkResponse(ProfileLink link) {
        return new ProfileLinkResponse(link.getId(), link.getLabel(), link.getUrl(), link.getSortOrder());
    }

    private ProfileSkillResponse toSkillResponse(ProfileSkill skill) {
        return new ProfileSkillResponse(skill.getId(), skill.getName(), skill.getCategory(), skill.getSortOrder());
    }

    private ProfileExperienceResponse toExperienceResponse(ProfileExperience experience) {
        return new ProfileExperienceResponse(experience.getId(), experience.getCompany(), experience.getTitle(), experience.getLocation(), experience.getStartDate(), experience.getEndDate(), experience.isCurrent(), experience.getDescription(), experience.getSortOrder());
    }

    private ProfileEducationResponse toEducationResponse(ProfileEducation education) {
        return new ProfileEducationResponse(education.getId(), education.getInstitution(), education.getDegree(), education.getFieldOfStudy(), education.getStartDate(), education.getEndDate(), education.getGrade(), education.getDescription(), education.getSortOrder());
    }

    private ProfileProjectResponse toProjectResponse(ProfileProject project) {
        return new ProfileProjectResponse(project.getId(), project.getName(), project.getDescription(), project.getProjectUrl(), project.getRepositoryUrl(), project.getTechStack(), project.getSortOrder());
    }

    private <T> T requireOwned(T entity, Long profileId, String errorMessage) {
        Long ownerProfileId;
        if (entity instanceof ProfileLink link) {
            ownerProfileId = link.getProfile().getId();
        } else if (entity instanceof ProfileSkill skill) {
            ownerProfileId = skill.getProfile().getId();
        } else if (entity instanceof ProfileExperience experience) {
            ownerProfileId = experience.getProfile().getId();
        } else if (entity instanceof ProfileEducation education) {
            ownerProfileId = education.getProfile().getId();
        } else if (entity instanceof ProfileProject project) {
            ownerProfileId = project.getProfile().getId();
        } else {
            throw new IllegalArgumentException("Unsupported profile child entity");
        }
        if (!Objects.equals(ownerProfileId, profileId)) {
            throw new ResourceNotFoundException(errorMessage);
        }
        return entity;
    }

    private record DefaultSection(String sectionKey, String displayName, Integer sortOrder) {
    }
}
