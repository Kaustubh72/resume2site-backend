package com.resume2site.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "SignupRequest", description = "Signup payload used when a new user creates an account at publish time.")
public record SignupRequest(
        @Schema(description = "User email address. Stored in normalized lowercase form.", example = "alice@example.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must be at most 255 characters")
        String email,

        @Schema(description = "User password. Must be 8-72 characters before hashing.", example = "password123")
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        String password,

        @Schema(description = "Display name shown in the user's portfolio/profile context.", example = "Alice Johnson")
        @NotBlank(message = "Full name is required")
        @Size(max = 255, message = "Full name must be at most 255 characters")
        String fullName
) {
}
