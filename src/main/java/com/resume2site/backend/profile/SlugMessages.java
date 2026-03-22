package com.resume2site.backend.profile;

public final class SlugMessages {

    public static final String SLUG_REQUIRED = "slug is required";
    public static final String SLUG_LOWERCASE_ONLY = "slug must use lowercase letters, numbers, and hyphens only";
    public static final String SLUG_LENGTH_INVALID = "slug must be between 3 and 40 characters";
    public static final String SLUG_FORMAT_INVALID = "slug must contain only lowercase letters, numbers, and hyphens";
    public static final String SLUG_RESERVED = "slug is reserved";
    public static final String SLUG_VALID = "slug is valid";
    public static final String SLUG_AVAILABLE = "Slug is available";
    public static final String SLUG_TAKEN = "Slug is already taken";
    public static final String DEFAULT_SUGGESTION_BASE = "site";

    private SlugMessages() {
    }
}
