package com.resume2site.backend.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.resume2site.backend.common.exception.BadRequestException;
import com.resume2site.backend.common.exception.UnauthorizedException;
import com.resume2site.backend.profile.domain.Profile;
import com.resume2site.backend.profile.domain.ProfileSection;
import com.resume2site.backend.profile.dto.ProfileDetailResponse;
import com.resume2site.backend.profile.dto.PublicProfileResponse;
import com.resume2site.backend.profile.dto.PublishProfileRequest;
import com.resume2site.backend.profile.dto.PublishProfileResponse;
import com.resume2site.backend.profile.dto.SlugAvailabilityResponse;
import com.resume2site.backend.profile.dto.UpdateProfileRequest;
import com.resume2site.backend.profile.dto.UpdateProfileSectionsRequest;
import com.resume2site.backend.profile.dto.UpdateProfileSlugRequest;
import com.resume2site.backend.profile.repository.ProfileEducationRepository;
import com.resume2site.backend.profile.repository.ProfileExperienceRepository;
import com.resume2site.backend.profile.repository.ProfileLinkRepository;
import com.resume2site.backend.profile.repository.ProfileProjectRepository;
import com.resume2site.backend.profile.repository.ProfileRepository;
import com.resume2site.backend.profile.repository.ProfileSectionRepository;
import com.resume2site.backend.profile.repository.ProfileSkillRepository;
import com.resume2site.backend.security.jwt.AuthenticatedUser;
import com.resume2site.backend.template.domain.Template;
import com.resume2site.backend.template.repository.TemplateRepository;
import com.resume2site.backend.user.domain.User;
import com.resume2site.backend.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock private ProfileRepository profileRepository;
    @Mock private TemplateRepository templateRepository;
    @Mock private ProfileSectionRepository profileSectionRepository;
    @Mock private ProfileLinkRepository profileLinkRepository;
    @Mock private ProfileSkillRepository profileSkillRepository;
    @Mock private ProfileExperienceRepository profileExperienceRepository;
    @Mock private ProfileEducationRepository profileEducationRepository;
    @Mock private ProfileProjectRepository profileProjectRepository;
    @Mock private UserRepository userRepository;
    @Mock private SlugService slugService;

    @InjectMocks private ProfileService profileService;

    private Profile anonymousProfile;

    @BeforeEach
    void setUp() {
        anonymousProfile = new Profile();
        anonymousProfile.setId(10L);
        anonymousProfile.setDraftToken("draft-token");
        anonymousProfile.setPublicationStatus("DRAFT");
    }


    @Test
    void getPublicProfileReturnsOnlyPublishedProfiles() {
        Template template = new Template();
        template.setId(3L);
        template.setCode("modern-stack");
        template.setName("Modern Stack");
        anonymousProfile.setTemplate(template);
        anonymousProfile.setSlug("john-dev");
        anonymousProfile.setPublicationStatus("PUBLISHED");

        when(profileRepository.findBySlugIgnoreCaseAndPublicationStatus("john-dev", "PUBLISHED"))
                .thenReturn(Optional.of(anonymousProfile));
        when(profileSectionRepository.findAllByProfileIdOrderBySortOrderAsc(10L))
                .thenReturn(List.of(section("summary", "Summary", 0)));
        when(profileLinkRepository.findAllByProfileIdOrderBySortOrderAsc(10L)).thenReturn(List.of());
        when(profileSkillRepository.findAllByProfileIdOrderBySortOrderAsc(10L)).thenReturn(List.of());
        when(profileExperienceRepository.findAllByProfileIdOrderBySortOrderAsc(10L)).thenReturn(List.of());
        when(profileEducationRepository.findAllByProfileIdOrderBySortOrderAsc(10L)).thenReturn(List.of());
        when(profileProjectRepository.findAllByProfileIdOrderBySortOrderAsc(10L)).thenReturn(List.of());

        PublicProfileResponse response = profileService.getPublicProfile("john-dev");

        assertThat(response.slug()).isEqualTo("john-dev");
        assertThat(response.template().code()).isEqualTo("modern-stack");
        assertThat(response.profile().sections()).hasSize(1);
    }

    @Test
    void getPublicProfileRejectsUnpublishedProfiles() {
        when(profileRepository.findBySlugIgnoreCaseAndPublicationStatus("john-dev", "PUBLISHED"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.getPublicProfile("john-dev"))
                .hasMessage("Published profile not found");
    }

    @Test
    void getProfileRequiresDraftTokenForAnonymousDrafts() {
        when(profileRepository.findById(10L)).thenReturn(Optional.of(anonymousProfile));

        assertThatThrownBy(() -> profileService.getProfile(10L, null, null))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("A valid draft token is required to access this profile");
    }

    @Test
    void getProfileReturnsDraftWhenTokenMatches() {
        when(profileRepository.findById(10L)).thenReturn(Optional.of(anonymousProfile));
        when(profileSectionRepository.findAllByProfileIdOrderBySortOrderAsc(10L))
                .thenReturn(List.of())
                .thenReturn(List.of(section("summary", "Summary", 0)));
        when(profileSectionRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(profileLinkRepository.findAllByProfileIdOrderBySortOrderAsc(10L)).thenReturn(List.of());
        when(profileSkillRepository.findAllByProfileIdOrderBySortOrderAsc(10L)).thenReturn(List.of());
        when(profileExperienceRepository.findAllByProfileIdOrderBySortOrderAsc(10L)).thenReturn(List.of());
        when(profileEducationRepository.findAllByProfileIdOrderBySortOrderAsc(10L)).thenReturn(List.of());
        when(profileProjectRepository.findAllByProfileIdOrderBySortOrderAsc(10L)).thenReturn(List.of());

        ProfileDetailResponse response = profileService.getProfile(10L, "draft-token", null);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.draftToken()).isEqualTo("draft-token");
        verify(profileSectionRepository).saveAll(any());
    }

    @Test
    void updateProfileRequiresOwnerWhenProfileBelongsToUser() {
        User owner = new User();
        owner.setId(77L);
        anonymousProfile.setUser(owner);
        when(profileRepository.findById(10L)).thenReturn(Optional.of(anonymousProfile));

        UpdateProfileRequest request = new UpdateProfileRequest("Name", "Headline", "Summary", "user@example.com", null, null, 3L);

        assertThatThrownBy(() -> profileService.updateProfile(10L, request, "draft-token", new AuthenticatedUser(99L, "other@example.com")))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("You do not have access to this profile");
    }

    @Test
    void updateProfileRejectsInactiveTemplate() {
        when(profileRepository.findById(10L)).thenReturn(Optional.of(anonymousProfile));
        Template template = new Template();
        template.setId(5L);
        template.setActive(false);
        when(templateRepository.findById(5L)).thenReturn(Optional.of(template));

        UpdateProfileRequest request = new UpdateProfileRequest("Name", "Headline", "Summary", "user@example.com", null, null, 5L);

        assertThatThrownBy(() -> profileService.updateProfile(10L, request, "draft-token", null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Template not found or inactive");
    }

    @Test
    void updateSectionsRejectsDuplicateSortOrder() {
        when(profileRepository.findById(10L)).thenReturn(Optional.of(anonymousProfile));
        when(profileSectionRepository.findAllByProfileIdOrderBySortOrderAsc(10L))
                .thenReturn(List.of(section("summary", "Summary", 0), section("skills", "Skills", 1)));

        UpdateProfileSectionsRequest request = new UpdateProfileSectionsRequest(List.of(
                new UpdateProfileSectionsRequest.SectionItem("summary", "About", true, 0),
                new UpdateProfileSectionsRequest.SectionItem("skills", "Skills", true, 0)
        ));

        assertThatThrownBy(() -> profileService.updateSections(10L, request, "draft-token", null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Duplicate sortOrder is not allowed: 0");
    }

    @Test
    void publishAttachesAnonymousProfileToAuthenticatedUserAndReturnsPublicUrl() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(42L, "owner@example.com");
        Template template = new Template();
        template.setId(3L);
        anonymousProfile.setTemplate(template);

        User user = new User();
        user.setId(42L);

        when(profileRepository.findById(10L)).thenReturn(Optional.of(anonymousProfile));
        when(userRepository.findById(42L)).thenReturn(Optional.of(user));
        when(slugService.requireUsableSlug("john-dev", 10L)).thenReturn("john-dev");
        when(profileRepository.save(anonymousProfile)).thenReturn(anonymousProfile);

        PublishProfileResponse response = profileService.publishProfile(10L, new PublishProfileRequest("john-dev"), "draft-token", authenticatedUser);

        assertThat(anonymousProfile.getUser()).isEqualTo(user);
        assertThat(anonymousProfile.getSlug()).isEqualTo("john-dev");
        assertThat(anonymousProfile.getPublicationStatus()).isEqualTo("PUBLISHED");
        assertThat(response.publicUrl()).isEqualTo("/u/john-dev");
    }

    @Test
    void publishRequiresTemplateBeforePublishing() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(42L, "owner@example.com");
        User owner = new User();
        owner.setId(42L);
        anonymousProfile.setUser(owner);

        when(profileRepository.findById(10L)).thenReturn(Optional.of(anonymousProfile));

        assertThatThrownBy(() -> profileService.publishProfile(10L, new PublishProfileRequest("john-dev"), null, authenticatedUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("templateId is required before publishing");

        verify(slugService, never()).requireUsableSlug(any(), any());
    }

    @Test
    void updateSlugRequiresPublishedProfile() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(42L, "owner@example.com");
        User owner = new User();
        owner.setId(42L);
        anonymousProfile.setUser(owner);

        when(profileRepository.findById(10L)).thenReturn(Optional.of(anonymousProfile));

        assertThatThrownBy(() -> profileService.updateSlug(10L, new UpdateProfileSlugRequest("john-dev"), authenticatedUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Profile must be published before updating slug");
    }

    @Test
    void slugAvailabilityReturnsSuggestionsWhenTaken() {
        SlugService service = new SlugService(profileRepository);
        when(profileRepository.existsBySlugIgnoreCase("john-dev")).thenReturn(true);
        when(profileRepository.existsBySlugIgnoreCase("john-dev-site")).thenReturn(false);
        when(profileRepository.existsBySlugIgnoreCase("john-dev-portfolio")).thenReturn(false);

        SlugAvailabilityResponse response = service.checkAvailability("john-dev");

        assertThat(response.valid()).isTrue();
        assertThat(response.available()).isFalse();
        assertThat(response.suggestions()).contains("john-dev-site", "john-dev-portfolio");
    }

    private ProfileSection section(String key, String displayName, int sortOrder) {
        ProfileSection section = new ProfileSection();
        section.setSectionKey(key);
        section.setDisplayName(displayName);
        section.setVisible(true);
        section.setSortOrder(sortOrder);
        section.setProfile(anonymousProfile);
        return section;
    }
}
