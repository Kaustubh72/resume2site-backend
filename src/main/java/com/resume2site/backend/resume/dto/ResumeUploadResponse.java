package com.resume2site.backend.resume.dto;

public record ResumeUploadResponse(
        Long id,
        String originalFileName,
        String contentType,
        Long fileSizeBytes,
        String parseStatus
) {
}
