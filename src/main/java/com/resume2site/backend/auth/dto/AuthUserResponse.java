package com.resume2site.backend.auth.dto;

public record AuthUserResponse(
        Long id,
        String email,
        String fullName
) {
}
