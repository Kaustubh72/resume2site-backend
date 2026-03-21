package com.resume2site.backend.profile;

import com.resume2site.backend.common.exception.BadRequestException;
import com.resume2site.backend.profile.dto.SlugAvailabilityResponse;
import com.resume2site.backend.profile.repository.ProfileRepository;
import java.text.Normalizer;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class SlugService {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 40;
    private static final List<String> RESERVED_SLUGS = List.of(
            "admin", "api", "app", "assets", "auth", "dashboard", "help", "home", "login",
            "logout", "me", "portfolio", "preview", "pricing", "profiles", "publish", "resume",
            "resumes", "settings", "signup", "site", "sites", "slug", "slugs", "static", "support",
            "templates", "u", "upload", "www"
    );

    private final ProfileRepository profileRepository;

    public SlugService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public SlugAvailabilityResponse checkAvailability(String rawValue) {
        String slug = normalize(rawValue);
        ValidationResult validation = validate(slug);
        if (!validation.valid()) {
            return new SlugAvailabilityResponse(slug, false, false, validation.message(), suggestAlternatives(slug, null));
        }

        boolean available = !profileRepository.existsBySlugIgnoreCase(slug);
        String message = available ? "Slug is available" : "Slug is already taken";
        List<String> suggestions = available ? List.of() : suggestAlternatives(slug, null);
        return new SlugAvailabilityResponse(slug, true, available, message, suggestions);
    }

    public String requireUsableSlug(String rawValue, Long currentProfileId) {
        String slug = normalize(rawValue);
        ValidationResult validation = validate(slug);
        if (!validation.valid()) {
            throw new BadRequestException(validation.message());
        }
        boolean exists = currentProfileId == null
                ? profileRepository.existsBySlugIgnoreCase(slug)
                : profileRepository.existsBySlugIgnoreCaseAndIdNot(slug, currentProfileId);
        if (exists) {
            throw new BadRequestException("Slug is already taken");
        }
        return slug;
    }

    public List<String> suggestionsFor(String rawValue, Long currentProfileId) {
        return suggestAlternatives(normalize(rawValue), currentProfileId);
    }

    private ValidationResult validate(String slug) {
        if (slug == null || slug.isBlank()) {
            return new ValidationResult(false, "slug is required");
        }
        if (slug.length() < MIN_LENGTH || slug.length() > MAX_LENGTH) {
            return new ValidationResult(false, "slug must be between 3 and 40 characters");
        }
        if (!VALID_PATTERN.matcher(slug).matches()) {
            return new ValidationResult(false, "Slug must contain only lowercase letters, numbers, and hyphens");
        }
        if (RESERVED_SLUGS.contains(slug)) {
            return new ValidationResult(false, "Slug is reserved");
        }
        return new ValidationResult(true, "Slug is valid");
    }

    private List<String> suggestAlternatives(String slug, Long currentProfileId) {
        String base = sanitizeBase(slug);
        if (base.length() < MIN_LENGTH) {
            base = (base + "site").substring(0, Math.min(MAX_LENGTH, Math.max(MIN_LENGTH, base.length() + 4)));
        }

        Set<String> suggestions = new LinkedHashSet<>();
        addIfAvailable(suggestions, base, currentProfileId);
        addIfAvailable(suggestions, trimToMax(base + "-site"), currentProfileId);
        addIfAvailable(suggestions, trimToMax(base + "-portfolio"), currentProfileId);
        for (int index = 2; suggestions.size() < 5 && index <= 20; index++) {
            addIfAvailable(suggestions, trimToMax(base + "-" + index), currentProfileId);
        }
        return suggestions.stream().limit(5).toList();
    }

    private void addIfAvailable(Set<String> suggestions, String candidate, Long currentProfileId) {
        ValidationResult validation = validate(candidate);
        if (!validation.valid()) {
            return;
        }
        boolean exists = currentProfileId == null
                ? profileRepository.existsBySlugIgnoreCase(candidate)
                : profileRepository.existsBySlugIgnoreCaseAndIdNot(candidate, currentProfileId);
        if (!exists) {
            suggestions.add(candidate);
        }
    }

    private String sanitizeBase(String value) {
        String normalized = normalize(value);
        if (normalized == null) {
            return "site";
        }
        String ascii = Normalizer.normalize(normalized, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "")
                .replaceAll("-+", "-");
        if (ascii.isBlank()) {
            return "site";
        }
        return trimToMax(ascii);
    }

    private String trimToMax(String value) {
        String trimmed = value.length() <= MAX_LENGTH ? value : value.substring(0, MAX_LENGTH);
        return trimmed.replaceAll("-+$", "");
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private record ValidationResult(boolean valid, String message) {
    }
}
