package com.resume2site.backend.security.jwt;

public record AuthenticatedUser(Long userId, String email) {
}
