package com.resume2site.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateProfileRequest", description = "Top-level editable profile fields updated by the portfolio editor.")
public record UpdateProfileRequest(
        @Schema(description = "Profile full name", example = "Alice Johnson")
        @Size(max = 255, message = "fullName must be at most 255 characters")
        String fullName,
        @Schema(description = "Short profile headline", example = "Software Engineer")
        @Size(max = 255, message = "headline must be at most 255 characters")
        String headline,
        @Schema(description = "Longer summary/about text", example = "Backend-focused engineer building Spring Boot systems.")
        @Size(max = 5000, message = "summary must be at most 5000 characters")
        String summary,
        @Schema(description = "Email address shown in profile data", example = "alice@example.com")
        @Email(message = "email must be a valid email address")
        @Size(max = 255, message = "email must be at most 255 characters")
        String email,
        @Schema(description = "Phone number shown in profile data", example = "+1-555-0100")
        @Size(max = 50, message = "phone must be at most 50 characters")
        String phone,
        @Schema(description = "Human-readable location string", example = "New York, NY")
        @Size(max = 255, message = "location must be at most 255 characters")
        String location,
        @Schema(description = "Selected template id. May remain null during draft editing but is required before publish.", example = "2")
        @Positive(message = "templateId must be a positive number")
        Long templateId
) {
}
