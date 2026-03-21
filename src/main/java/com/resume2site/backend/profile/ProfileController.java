package com.resume2site.backend.profile;

import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.profile.dto.ProfileSummaryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @GetMapping("/foundation-status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProfileSummaryResponse> foundationStatus() {
        // TODO: Replace placeholder endpoint with profile CRUD endpoints in the next implementation step.
        return new ApiResponse<>(new ProfileSummaryResponse(
                null,
                null,
                null,
                null,
                "FOUNDATION_ONLY",
                null,
                null
        ));
    }
}
