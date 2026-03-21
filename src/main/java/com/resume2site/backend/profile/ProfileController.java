package com.resume2site.backend.profile;

import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.profile.dto.ProfileSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles")
@Tag(name = "Profiles", description = "Profile management operations")
public class ProfileController {

    @Operation(summary = "Get foundation status")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved foundation status"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
