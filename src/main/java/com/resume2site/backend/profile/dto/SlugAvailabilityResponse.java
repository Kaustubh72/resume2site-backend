package com.resume2site.backend.profile.dto;

import java.util.List;

public record SlugAvailabilityResponse(
        String value,
        boolean valid,
        boolean available,
        String message,
        List<String> suggestions
) {
}
