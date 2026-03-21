package com.resume2site.backend.health;

import com.resume2site.backend.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health", description = "Health check operations")
public class HealthController {

    @Operation(summary = "Health check")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Service is up and running"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Service is down")
    })
    @GetMapping
    public ApiResponse<Map<String, Object>> health() {
        return new ApiResponse<>(Map.of(
                "status", "UP",
                "service", "resume2site-backend",
                "timestamp", Instant.now()
        ));
    }
}
