package com.resume2site.backend.common.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponse", description = "Standard success envelope used by the Resume2Site backend. The actual payload is returned inside the `data` field.")
public record ApiResponse<@Schema(description = "Endpoint-specific response payload") T>(
        @Schema(description = "Endpoint-specific response payload")
        T data
) {
}
