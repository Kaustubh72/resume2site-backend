package com.resume2site.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthUserResponse", description = "Authenticated user summary used in auth responses and `/me`.")
public record AuthUserResponse(
        @Schema(description = "User id", example = "1")
        Long id,
        @Schema(description = "Normalized login email address", example = "alice@example.com")
        String email,
        @Schema(description = "Display name captured during signup", example = "Alice Johnson")
        String fullName
) {
}
