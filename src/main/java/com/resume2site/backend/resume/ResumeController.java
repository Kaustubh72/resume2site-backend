package com.resume2site.backend.resume;

import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.resume.dto.ResumeParseResponse;
import com.resume2site.backend.resume.dto.ResumeUploadResponse;
import com.resume2site.backend.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resumes")
@Tag(name = "Resumes", description = "Resume upload and processing operations")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @Operation(summary = "Upload an anonymous resume for MVP parsing")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resume uploaded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid resume file", content = @Content(schema = @Schema(implementation = Object.class)))
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ResumeUploadResponse> upload(@RequestPart("file") MultipartFile file) {
        return new ApiResponse<>(resumeService.upload(file));
    }

    @Operation(summary = "Parse a previously uploaded resume into a draft profile")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resume parsed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Resume upload not found")
    })
    @PostMapping("/{resumeUploadId}/parse")
    public ApiResponse<ResumeParseResponse> parse(@PathVariable Long resumeUploadId) {
        return new ApiResponse<>(resumeService.parse(resumeUploadId));
    }
}
