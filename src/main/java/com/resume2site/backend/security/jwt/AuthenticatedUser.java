package com.resume2site.backend.security.jwt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthenticatedUser", description = "Authenticated principal extracted from a JWT token and injected into secured controller methods.")
public record AuthenticatedUser(
        @Schema(description = "Authenticated user id", example = "1") Long userId,
        @Schema(description = "Authenticated user email", example = "alice@example.com") String email
) {
}
