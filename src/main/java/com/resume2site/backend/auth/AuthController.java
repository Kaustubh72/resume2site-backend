package com.resume2site.backend.auth;

import com.resume2site.backend.auth.dto.AuthResponse;
import com.resume2site.backend.auth.dto.AuthUserResponse;
import com.resume2site.backend.auth.dto.LoginRequest;
import com.resume2site.backend.auth.dto.SignupRequest;
import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.security.jwt.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication operations")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @Operation(summary = "User signup")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successful signup"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ApiResponse<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        return new ApiResponse<>(authService.signup(request));
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successful login"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return new ApiResponse<>(authService.login(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successful retrieval of user data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ApiResponse<AuthUserResponse> me(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return new ApiResponse<>(authService.me(authenticatedUser.userId()));
    }
}
