package com.resume2site.backend.resume.dto;

import com.resume2site.backend.profile.dto.ProfileSummaryResponse;

public record ResumeParseResponse(
        Long resumeUploadId,
        String parseStatus,
        ProfileSummaryResponse profile
) {
}
