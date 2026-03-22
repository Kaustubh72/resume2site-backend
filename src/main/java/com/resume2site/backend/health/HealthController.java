package com.resume2site.backend.health;

import com.resume2site.backend.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health", description = "Simple health endpoint for local/dev checks and uptime verification.")
public class HealthController {

    @Operation(summary = "Health check", description = "Returns a minimal liveness payload for the backend service.")
    @GetMapping
    public ApiResponse<Map<String, Object>> health() {
        return new ApiResponse<>(Map.of(
                "status", "UP",
                "service", "resume2site-backend",
                "timestamp", Instant.now()
        ));
    }
}
