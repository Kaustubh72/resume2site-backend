package com.resume2site.backend.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

@Schema(name = "ApiErrorResponse", description = "Standard error envelope returned when the backend rejects a request or encounters a failure.")
public record ApiErrorResponse(
        @Schema(description = "UTC timestamp when the error response was generated", example = "2026-03-22T10:15:30Z")
        Instant timestamp,
        @Schema(description = "HTTP status code", example = "400")
        int status,
        @Schema(description = "HTTP status text", example = "Bad Request")
        String error,
        @Schema(description = "Human-readable summary of the error", example = "Validation failed")
        String message,
        @Schema(description = "Request path that produced the error", example = "/api/profiles/10/publish")
        String path,
        @Schema(description = "Optional per-field validation errors when validation fails")
        List<ApiFieldError> fieldErrors
) {
    @Schema(name = "ApiFieldError", description = "Validation error for one specific request field.")
    public record ApiFieldError(
            @Schema(description = "Field name that failed validation", example = "slug")
            String field,
            @Schema(description = "Validation failure reason for the field", example = "slug must contain only lowercase letters, numbers, and hyphens")
            String message
    ) {
    }
}
