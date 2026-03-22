package com.resume2site.backend.profile;

import com.resume2site.backend.common.api.ApiErrorResponse;
import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.profile.dto.*;
import com.resume2site.backend.security.jwt.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@Tag(name = "Profiles", description = "Draft profile editing, nested section CRUD, publish, and slug-management endpoints.")
public class ProfileController {

    private static final String DRAFT_TOKEN_HEADER = "X-Draft-Token";

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{profileId}")
    @Operation(summary = "Get one editable profile", description = "Returns the full editable profile payload. Access is granted either via authenticated ownership or a valid anonymous draft token.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile returned", content = @Content(schema = @Schema(implementation = ProfileDetailResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Missing auth or draft token for this profile", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Profile not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ApiResponse<ProfileDetailResponse> getProfile(
            @Parameter(description = "Profile id", example = "10", required = true) @PathVariable Long profileId,
            @Parameter(description = "Anonymous draft ownership token required when editing a draft before authentication", example = "high-entropy-draft-token")
            @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.getProfile(profileId, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}")
    @Operation(summary = "Update top-level profile fields", description = "Updates top-level editable fields such as name, headline, summary, contact data, location, and template selection.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<ProfileDetailResponse> updateProfile(
            @Parameter(description = "Profile id", example = "10", required = true) @PathVariable Long profileId,
            @Valid @RequestBody UpdateProfileRequest request,
            @Parameter(description = "Anonymous draft token for unauthenticated drafts", example = "high-entropy-draft-token")
            @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateProfile(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/sections")
    @Operation(summary = "Update section visibility and order", description = "Bulk updates logical section configuration including display label, visibility, and sort order.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<List<ProfileSectionResponse>> updateSections(
            @Parameter(description = "Profile id", example = "10", required = true) @PathVariable Long profileId,
            @Valid @RequestBody UpdateProfileSectionsRequest request,
            @Parameter(description = "Anonymous draft token for unauthenticated drafts", example = "high-entropy-draft-token")
            @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateSections(profileId, request, draftToken, authenticatedUser));
    }

    @PostMapping("/{profileId}/publish")
    @Operation(summary = "Publish a profile", description = "Attaches an anonymous draft to the authenticated user if needed, validates slug/template requirements, and marks the profile as published.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<PublishProfileResponse> publishProfile(
            @Parameter(description = "Profile id", example = "10", required = true) @PathVariable Long profileId,
            @Valid @RequestBody PublishProfileRequest request,
            @Parameter(description = "Draft token needed when publishing an anonymous draft for the first time", example = "high-entropy-draft-token")
            @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.publishProfile(profileId, request, draftToken, authenticatedUser));
    }

    @PostMapping("/{profileId}/republish")
    @Operation(summary = "Republish an existing profile", description = "Updates the public slug/publication metadata for a profile that already belongs to the authenticated user.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<PublishProfileResponse> republishProfile(
            @Parameter(description = "Profile id", example = "10", required = true) @PathVariable Long profileId,
            @Valid @RequestBody PublishProfileRequest request,
            @Parameter(description = "Draft token for a still-anonymous draft if applicable", example = "high-entropy-draft-token")
            @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.republishProfile(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/slug")
    @Operation(summary = "Change slug of a published profile", description = "Updates the slug for a profile that is already published and owned by the authenticated user.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<PublishProfileResponse> updateSlug(
            @Parameter(description = "Profile id", example = "10", required = true) @PathVariable Long profileId,
            @Valid @RequestBody UpdateProfileSlugRequest request,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateSlug(profileId, request, authenticatedUser));
    }

    @PostMapping("/{profileId}/links")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create link entry", description = "Adds one external link item to the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<ProfileLinkResponse> createLink(@PathVariable Long profileId,
                                                       @Valid @RequestBody UpsertProfileLinkRequest request,
                                                       @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                       @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.createLink(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/links/{linkId}")
    @Operation(summary = "Update link entry", description = "Updates one external link item on the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<ProfileLinkResponse> updateLink(@PathVariable Long profileId,
                                                       @PathVariable Long linkId,
                                                       @Valid @RequestBody UpsertProfileLinkRequest request,
                                                       @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                       @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateLink(profileId, linkId, request, draftToken, authenticatedUser));
    }

    @DeleteMapping("/{profileId}/links/{linkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete link entry", description = "Deletes one external link item from the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteLink(@PathVariable Long profileId,
                           @PathVariable Long linkId,
                           @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                           @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        profileService.deleteLink(profileId, linkId, draftToken, authenticatedUser);
    }

    @PostMapping("/{profileId}/skills")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create skill entry", description = "Adds one skill item to the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<ProfileSkillResponse> createSkill(@PathVariable Long profileId,
                                                         @Valid @RequestBody UpsertProfileSkillRequest request,
                                                         @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                         @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.createSkill(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/skills/{skillId}")
    @Operation(summary = "Update skill entry", description = "Updates one skill item on the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<ProfileSkillResponse> updateSkill(@PathVariable Long profileId,
                                                         @PathVariable Long skillId,
                                                         @Valid @RequestBody UpsertProfileSkillRequest request,
                                                         @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                         @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateSkill(profileId, skillId, request, draftToken, authenticatedUser));
    }

    @DeleteMapping("/{profileId}/skills/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete skill entry", description = "Deletes one skill item from the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteSkill(@PathVariable Long profileId,
                            @PathVariable Long skillId,
                            @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        profileService.deleteSkill(profileId, skillId, draftToken, authenticatedUser);
    }

    @PostMapping("/{profileId}/experiences")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create experience entry", description = "Adds one work experience entry to the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<ProfileExperienceResponse> createExperience(@PathVariable Long profileId,
                                                                   @Valid @RequestBody UpsertProfileExperienceRequest request,
                                                                   @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                                   @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.createExperience(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/experiences/{experienceId}")
    @Operation(summary = "Update experience entry", description = "Updates one work experience entry on the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<ProfileExperienceResponse> updateExperience(@PathVariable Long profileId,
                                                                   @PathVariable Long experienceId,
                                                                   @Valid @RequestBody UpsertProfileExperienceRequest request,
                                                                   @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                                   @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateExperience(profileId, experienceId, request, draftToken, authenticatedUser));
    }

    @DeleteMapping("/{profileId}/experiences/{experienceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete experience entry", description = "Deletes one work experience entry from the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteExperience(@PathVariable Long profileId,
                                 @PathVariable Long experienceId,
                                 @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                 @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        profileService.deleteExperience(profileId, experienceId, draftToken, authenticatedUser);
    }

    @PostMapping("/{profileId}/education")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create education entry", description = "Adds one education entry to the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<ProfileEducationResponse> createEducation(@PathVariable Long profileId,
                                                                 @Valid @RequestBody UpsertProfileEducationRequest request,
                                                                 @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                                 @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.createEducation(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/education/{educationId}")
    @Operation(summary = "Update education entry", description = "Updates one education entry on the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<ProfileEducationResponse> updateEducation(@PathVariable Long profileId,
                                                                 @PathVariable Long educationId,
                                                                 @Valid @RequestBody UpsertProfileEducationRequest request,
                                                                 @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                                 @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateEducation(profileId, educationId, request, draftToken, authenticatedUser));
    }

    @DeleteMapping("/{profileId}/education/{educationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete education entry", description = "Deletes one education entry from the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteEducation(@PathVariable Long profileId,
                                @PathVariable Long educationId,
                                @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        profileService.deleteEducation(profileId, educationId, draftToken, authenticatedUser);
    }

    @PostMapping("/{profileId}/projects")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create project entry", description = "Adds one project entry to the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<ProfileProjectResponse> createProject(@PathVariable Long profileId,
                                                             @Valid @RequestBody UpsertProfileProjectRequest request,
                                                             @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                             @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.createProject(profileId, request, draftToken, authenticatedUser));
    }

    @PutMapping("/{profileId}/projects/{projectId}")
    @Operation(summary = "Update project entry", description = "Updates one project entry on the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<ProfileProjectResponse> updateProject(@PathVariable Long profileId,
                                                             @PathVariable Long projectId,
                                                             @Valid @RequestBody UpsertProfileProjectRequest request,
                                                             @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                                                             @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(profileService.updateProject(profileId, projectId, request, draftToken, authenticatedUser));
    }

    @DeleteMapping("/{profileId}/projects/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete project entry", description = "Deletes one project entry from the profile.", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteProject(@PathVariable Long profileId,
                              @PathVariable Long projectId,
                              @RequestHeader(value = DRAFT_TOKEN_HEADER, required = false) String draftToken,
                              @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        profileService.deleteProject(profileId, projectId, draftToken, authenticatedUser);
    }
}
