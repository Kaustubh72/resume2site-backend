package com.resume2site.backend.profile;

import com.resume2site.backend.common.api.ApiErrorResponse;
import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.profile.dto.SlugAvailabilityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slugs")
@Validated
@Tag(name = "Slugs", description = "Public endpoints used to validate and pre-check profile slug availability before publish.")
public class SlugController {

    private final SlugService slugService;

    public SlugController(SlugService slugService) {
        this.slugService = slugService;
    }

    @GetMapping("/check")
    @Operation(summary = "Check slug validity and availability", description = "Normalizes a candidate slug, validates format/reserved-word rules, checks database uniqueness, and returns optional suggestions when unavailable.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Slug check completed", content = @Content(schema = @Schema(implementation = SlugAvailabilityResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Missing or invalid query parameter", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ApiResponse<SlugAvailabilityResponse> check(
            @Parameter(description = "Candidate public slug to validate", example = "alice-johnson", required = true)
            @RequestParam("value") @NotBlank(message = "value is required") String value) {
        return new ApiResponse<>(slugService.checkAvailability(value));
    }
}
