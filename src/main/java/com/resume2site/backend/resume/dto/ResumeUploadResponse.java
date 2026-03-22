package com.resume2site.backend.resume.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ResumeUploadResponse", description = "Metadata returned immediately after a resume file is uploaded.")
public record ResumeUploadResponse(
        @Schema(description = "Resume upload id used in the subsequent parse request", example = "1")
        Long id,
        @Schema(description = "Original client-side file name", example = "resume.pdf")
        String originalFileName,
        @Schema(description = "Normalized MIME type accepted by the backend", example = "application/pdf")
        String contentType,
        @Schema(description = "Uploaded file size in bytes", example = "245678")
        Long fileSizeBytes,
        @Schema(description = "Current upload processing state", example = "UPLOADED")
        String parseStatus
) {
}
