package com.resume2site.backend.health;

import com.resume2site.backend.common.api.ApiResponse;
import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ApiResponse<Map<String, Object>> health() {
        return new ApiResponse<>(Map.of(
                "status", "UP",
                "service", "resume2site-backend",
                "timestamp", Instant.now()
        ));
    }
}
