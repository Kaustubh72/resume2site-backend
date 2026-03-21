package com.resume2site.backend.profile;

import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.profile.dto.*;
import com.resume2site.backend.security.jwt.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@Tag(name = "Profiles", description = "Profile management operations")
public class ProfileController {

    private static final String DRAFT_TOKEN_HEADER = "X-Draft-Token";

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{profileId}")
    @Operation(summary = "Get a draft profile by id")
    public ApiResponse<ProfileDetailResponse> getProfile(@PathVariable Long profileId,
                                                         @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                         @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.getProfile(profileId, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}")
    public ApiResponse<ProfileDetailResponse> updateProfile(@PathVariable Long profileId,
                                                            @Valid @RequestBody UpdateProfileRequest request,
                                                            @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateProfile(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/sections")
    public ApiResponse<List<ProfileSectionResponse>> updateSections(@PathVariable Long profileId,
                                                                    @Valid @RequestBody UpdateProfileSectionsRequest request,
                                                                    @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                                    @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateSections(profileId, request, draftToken, authenticatedUser));
    }

    @PostMapping("/{profileId}/publish")
    public ApiResponse<PublishProfileResponse> publishProfile(@PathVariable Long profileId,
                                                              @Valid @RequestBody PublishProfileRequest request,
                                                              @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                              @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.publishProfile(profileId, request, draftToken, authenticatedUser));
    }

    @PostMapping("/{profileId}/republish")
    public ApiResponse<PublishProfileResponse> republishProfile(@PathVariable Long profileId,
                                                                @Valid @RequestBody PublishProfileRequest request,
                                                                @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                                @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.republishProfile(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/slug")
    public ApiResponse<PublishProfileResponse> updateSlug(@PathVariable Long profileId,
                                                          @Valid @RequestBody UpdateProfileSlugRequest request,
                                                          @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateSlug(profileId, request, authenticatedUser));
    }

    @PostMapping("/{profileId}/links")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProfileLinkResponse> createLink(@PathVariable Long profileId,
                                                       @Valid @RequestBody UpsertProfileLinkRequest request,
                                                       @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                       @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.createLink(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/links/{linkId}")
    public ApiResponse<ProfileLinkResponse> updateLink(@PathVariable Long profileId,
                                                       @PathVariable Long linkId,
                                                       @Valid @RequestBody UpsertProfileLinkRequest request,
                                                       @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                       @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateLink(profileId, linkId, request, draftToken, authenticatedUser));
    }

    @DeleteMapping("/{profileId}/links/{linkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLink(@PathVariable Long profileId,
                           @PathVariable Long linkId,
                           @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                           @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        profileService.deleteLink(profileId, linkId, draftToken, authenticatedUser);
    }

    @PostMapping("/{profileId}/skills")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProfileSkillResponse> createSkill(@PathVariable Long profileId,
                                                         @Valid @RequestBody UpsertProfileSkillRequest request,
                                                         @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                         @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.createSkill(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/skills/{skillId}")
    public ApiResponse<ProfileSkillResponse> updateSkill(@PathVariable Long profileId,
                                                         @PathVariable Long skillId,
                                                         @Valid @RequestBody UpsertProfileSkillRequest request,
                                                         @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                         @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateSkill(profileId, skillId, request, draftToken, authenticatedUser));
    }

    @DeleteMapping("/{profileId}/skills/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkill(@PathVariable Long profileId,
                            @PathVariable Long skillId,
                            @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        profileService.deleteSkill(profileId, skillId, draftToken, authenticatedUser);
    }

    @PostMapping("/{profileId}/experiences")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProfileExperienceResponse> createExperience(@PathVariable Long profileId,
                                                                   @Valid @RequestBody UpsertProfileExperienceRequest request,
                                                                   @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                                   @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.createExperience(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/experiences/{experienceId}")
    public ApiResponse<ProfileExperienceResponse> updateExperience(@PathVariable Long profileId,
                                                                   @PathVariable Long experienceId,
                                                                   @Valid @RequestBody UpsertProfileExperienceRequest request,
                                                                   @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                                   @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateExperience(profileId, experienceId, request, draftToken, authenticatedUser));
    }

    @DeleteMapping("/{profileId}/experiences/{experienceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExperience(@PathVariable Long profileId,
                                 @PathVariable Long experienceId,
                                 @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                 @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        profileService.deleteExperience(profileId, experienceId, draftToken, authenticatedUser);
    }

    @PostMapping("/{profileId}/education")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProfileEducationResponse> createEducation(@PathVariable Long profileId,
                                                                 @Valid @RequestBody UpsertProfileEducationRequest request,
                                                                 @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                                 @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.createEducation(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/education/{educationId}")
    public ApiResponse<ProfileEducationResponse> updateEducation(@PathVariable Long profileId,
                                                                 @PathVariable Long educationId,
                                                                 @Valid @RequestBody UpsertProfileEducationRequest request,
                                                                 @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                                 @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateEducation(profileId, educationId, request, draftToken, authenticatedUser));
    }

    @DeleteMapping("/{profileId}/education/{educationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEducation(@PathVariable Long profileId,
                                @PathVariable Long educationId,
                                @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        profileService.deleteEducation(profileId, educationId, draftToken, authenticatedUser);
    }

    @PostMapping("/{profileId}/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProfileProjectResponse> createProject(@PathVariable Long profileId,
                                                             @Valid @RequestBody UpsertProfileProjectRequest request,
                                                             @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                             @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.createProject(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/projects/{projectId}")
    public ApiResponse<ProfileProjectResponse> updateProject(@PathVariable Long profileId,
                                                             @PathVariable Long projectId,
                                                             @Valid @RequestBody UpsertProfileProjectRequest request,
                                                             @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                             @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateProject(profileId, projectId, request, draftToken, authenticatedUser));
    }

    @DeleteMapping("/{profileId}/projects/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long profileId,
                              @PathVariable Long projectId,
                              @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                              @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        profileService.deleteProject(profileId, projectId, draftToken, authenticatedUser);
    }
}
