package com.resume2site.backend.resume.dto;

import com.resume2site.backend.profile.dto.ProfileSummaryResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ResumeParseResponse", description = "Result returned after converting an uploaded resume into an editable draft profile.")
public record ResumeParseResponse(
        @Schema(description = "Resume upload id that was parsed", example = "1")
        Long resumeUploadId,
        @Schema(description = "Current parse status after the operation completes", example = "PARSED")
        String parseStatus,
        @Schema(description = "Minimal draft profile summary created from the parsed resume")
        ProfileSummaryResponse profile
) {
}
