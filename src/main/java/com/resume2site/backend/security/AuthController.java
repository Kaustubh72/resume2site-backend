package com.resume2site.backend.security;

import com.resume2site.backend.common.api.ApiResponse;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/foundation-status")
    public ApiResponse<Map<String, String>> foundationStatus() {
        // TODO: Implement signup, login, and me endpoints in the next backend step.
        return new ApiResponse<>(Map.of("status", "auth foundation ready"));
    }
}
