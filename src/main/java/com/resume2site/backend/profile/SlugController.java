package com.resume2site.backend.profile;

import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.profile.dto.SlugAvailabilityResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slugs")
@Validated
public class SlugController {

    private final SlugService slugService;

    public SlugController(SlugService slugService) {
        this.slugService = slugService;
    }

    @GetMapping("/check")
    public ApiResponse<SlugAvailabilityResponse> check(@RequestParam("value") @NotBlank(message = "value is required") String value) {
        return new ApiResponse<>(slugService.checkAvailability(value));
    }
}
