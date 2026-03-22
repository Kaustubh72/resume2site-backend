package com.resume2site.backend.profile;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlugRules {

    public static final Pattern VALID_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");
    public static final int MIN_LENGTH = 3;
    public static final int MAX_LENGTH = 40;
    public static final List<String> RESERVED_SLUGS = List.of(
            "admin", "api", "app", "assets", "auth", "dashboard", "help", "home", "login",
            "logout", "me", "portfolio", "preview", "pricing", "profiles", "publish", "resume",
            "resumes", "settings", "signup", "site", "sites", "slug", "slugs", "static", "support",
            "templates", "u", "upload", "www"
    );

    private SlugRules() {
    }

    public static String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }

    public static ValidationResult validate(String value) {
        String slug = normalize(value);
        if (slug == null || slug.isBlank()) {
            return new ValidationResult(false, "slug is required");
        }
        if (!slug.equals(value.trim())) {
            return new ValidationResult(false, "slug must use lowercase letters, numbers, and hyphens only");
        }
        if (slug.length() < MIN_LENGTH || slug.length() > MAX_LENGTH) {
            return new ValidationResult(false, "slug must be between 3 and 40 characters");
        }
        if (!VALID_PATTERN.matcher(slug).matches()) {
            return new ValidationResult(false, "slug must contain only lowercase letters, numbers, and hyphens");
        }
        if (RESERVED_SLUGS.contains(slug)) {
            return new ValidationResult(false, "slug is reserved");
        }
        return new ValidationResult(true, "slug is valid");
    }

    public static String sanitizeBase(String value) {
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

    public static String trimToMax(String value) {
        String trimmed = value.length() <= MAX_LENGTH ? value : value.substring(0, MAX_LENGTH);
        return trimmed.replaceAll("-+$", "");
    }

    public record ValidationResult(boolean valid, String message) {
    }
}
