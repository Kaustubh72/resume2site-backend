package com.resume2site.backend.auth;

import com.resume2site.backend.auth.dto.AuthResponse;
import com.resume2site.backend.auth.dto.AuthUserResponse;
import com.resume2site.backend.auth.dto.LoginRequest;
import com.resume2site.backend.auth.dto.SignupRequest;
import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.security.jwt.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ApiResponse<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        return new ApiResponse<>(authService.signup(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return new ApiResponse<>(authService.login(request));
    }

    @GetMapping("/me")
    public ApiResponse<AuthUserResponse> me(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(authService.me(authenticatedUser.userId()));
    }
}
