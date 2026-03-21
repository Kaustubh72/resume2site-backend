package com.resume2site.backend.profile;

import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.profile.dto.PublicProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@Tag(name = "Public Profiles", description = "Public portfolio rendering APIs")
public class PublicProfileController {

    private final ProfileService profileService;

    public PublicProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Operation(summary = "Get a published public profile by slug")
    @GetMapping("/{slug}")
    public ApiResponse<PublicProfileResponse> getPublicProfile(@PathVariable String slug) {
        return new ApiResponse<>(profileService.getPublicProfile(slug));
    }
}
