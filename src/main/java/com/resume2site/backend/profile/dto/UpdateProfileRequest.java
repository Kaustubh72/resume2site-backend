package com.resume2site.backend.profile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(max = 255, message = "fullName must be at most 255 characters")
        String fullName,
        @Size(max = 255, message = "headline must be at most 255 characters")
        String headline,
        @Size(max = 5000, message = "summary must be at most 5000 characters")
        String summary,
        @Email(message = "email must be a valid email address")
        @Size(max = 255, message = "email must be at most 255 characters")
        String email,
        @Size(max = 50, message = "phone must be at most 50 characters")
        String phone,
        @Size(max = 255, message = "location must be at most 255 characters")
        String location,
        @NotNull(message = "templateId is required")
        Long templateId
) {
}
