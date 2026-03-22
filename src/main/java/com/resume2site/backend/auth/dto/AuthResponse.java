package com.resume2site.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthResponse", description = "Authentication response returned after signup or login.")
public record AuthResponse(
        @Schema(description = "JWT access token used for authenticated API calls", example = "eyJhbGciOiJIUzI1NiJ9...")
        String accessToken,
        @Schema(description = "Token type prefix expected in the Authorization header", example = "Bearer")
        String tokenType,
        @Schema(description = "Access token time-to-live in seconds", example = "3600")
        long expiresInSeconds,
        @Schema(description = "Authenticated user summary")
        AuthUserResponse user
) {
}
