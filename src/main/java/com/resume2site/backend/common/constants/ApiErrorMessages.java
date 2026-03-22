package com.resume2site.backend.common.constants;

public final class ApiErrorMessages {

    public static final String VALIDATION_FAILED = "Validation failed";
    public static final String DATA_CONFLICT = "Request could not be completed because it conflicts with existing data";
    public static final String SLUG_TAKEN = "Slug is already taken";
    public static final String EMAIL_EXISTS = "An account with this email already exists";
    public static final String UPLOAD_LIMIT_EXCEEDED = "Uploaded file exceeds configured limit";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";

    private ApiErrorMessages() {
    }
}
