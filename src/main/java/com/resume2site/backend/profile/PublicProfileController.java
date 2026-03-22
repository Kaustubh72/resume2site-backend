package com.resume2site.backend.profile;

import com.resume2site.backend.common.api.ApiErrorResponse;
import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.profile.dto.PublicProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@Tag(name = "Public Profiles", description = "Public portfolio rendering APIs used by the frontend route `/u/{slug}`.")
public class PublicProfileController {

    private final ProfileService profileService;

    public PublicProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Operation(summary = "Get a published profile by slug", description = "Returns the public-safe profile payload for a published slug. Unpublished or missing profiles return 404.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Published profile returned", content = @Content(schema = @Schema(implementation = PublicProfileResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Slug not found or not published", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{slug}")
    public ApiResponse<PublicProfileResponse> getPublicProfile(
            @Parameter(description = "Published portfolio slug", example = "alice-johnson", required = true)
            @PathVariable String slug) {
        return new ApiResponse<>(profileService.getPublicProfile(slug));
    }
}
