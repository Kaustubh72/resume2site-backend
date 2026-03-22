package com.resume2site.backend.resume;

import com.resume2site.backend.profile.ProfileConstants;
import java.util.List;

public final class ResumeConstants {

    public static final List<String> FALLBACK_CONTENT_TYPES = List.of(
            "application/octet-stream",
            "binary/octet-stream"
    );
    public static final String STATUS_UPLOADED = "UPLOADED";
    public static final String STATUS_PARSING = "PARSING";
    public static final String STATUS_PARSED = "PARSED";
    public static final String STATUS_FAILED = "FAILED";
    public static final String MESSAGE_RESUME_UPLOAD_NOT_FOUND = "Resume upload not found";
    public static final String MESSAGE_PARSED_PROFILE_NOT_FOUND = "Parsed profile not found for resume upload";
    public static final String MESSAGE_NO_READABLE_TEXT = "Uploaded resume did not contain readable text";
    public static final String MESSAGE_RESUME_FILE_REQUIRED = "Resume file is required";
    public static final String MESSAGE_RESUME_SIZE_LIMIT = "Resume file exceeds the allowed size limit";
    public static final String MESSAGE_RESUME_FILE_NAME_REQUIRED = "Resume file must include a valid file name";
    public static final String MESSAGE_RESUME_EXTENSION_REQUIRED = "Resume file must include a valid extension";
    public static final String MESSAGE_RESUME_FILE_TYPE_UNSUPPORTED = "Uploaded file type is not supported";
    public static final String MESSAGE_ONLY_PDF_DOCX_SUPPORTED = "Only PDF and DOCX resume uploads are supported";
    public static final String MESSAGE_STORE_UPLOAD_FAILED = "Unable to store uploaded resume";
    public static final String MESSAGE_EXTRACT_TEXT_FAILED = "Unable to extract text from the uploaded resume";
    public static final String TEMP_DIRECTORY_PREFIX = "resume2site-upload-";
    public static final String DEFAULT_EMPTY_SLUG_SUGGESTION_BASE = "site";
    public static final String PROFILE_STATUS_DRAFT = ProfileConstants.STATUS_DRAFT;

    private ResumeConstants() {
    }
}
