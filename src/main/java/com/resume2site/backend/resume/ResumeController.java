package com.resume2site.backend.resume;

import com.resume2site.backend.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resumes")
@Tag(name = "Resumes", description = "Resume upload and processing operations")
public class ResumeController {

    @Operation(summary = "Get foundation status")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved foundation status"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/foundation-status")
    public ApiResponse<Map<String, String>> foundationStatus() {
        // TODO: Implement anonymous upload and parsing pipeline in the next module.
        return new ApiResponse<>(Map.of("status", "resume foundation ready"));
    }
}
