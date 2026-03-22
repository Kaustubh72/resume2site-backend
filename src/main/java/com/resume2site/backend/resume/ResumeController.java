package com.resume2site.backend.resume;

import com.resume2site.backend.common.api.ApiErrorResponse;
import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.resume.dto.ResumeParseResponse;
import com.resume2site.backend.resume.dto.ResumeUploadResponse;
import com.resume2site.backend.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Resumes", description = "Anonymous resume upload and parsing endpoints used at the start of the MVP flow.")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @Operation(summary = "Upload a resume", description = "Accepts a PDF or DOCX resume upload without requiring authentication. The returned upload id is then used to trigger parsing.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resume uploaded successfully", content = @Content(schema = @Schema(implementation = ResumeUploadResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid resume upload", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "413", description = "Resume file too large", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ResumeUploadResponse> upload(
            @Parameter(description = "Resume file in PDF or DOCX format", required = true)
            @RequestPart("file") MultipartFile file) {
        return new ApiResponse<>(resumeService.upload(file));
    }

    @Operation(summary = "Parse an uploaded resume", description = "Extracts text from a previously uploaded resume and creates an editable draft profile. The response contains both the profile id and anonymous draft token.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resume parsed successfully", content = @Content(schema = @Schema(implementation = ResumeParseResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Uploaded file is unreadable or failed validation", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Resume upload not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Resume text extraction failed", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/{resumeUploadId}/parse")
    public ApiResponse<ResumeParseResponse> parse(
            @Parameter(description = "Resume upload id returned from `/api/resumes/upload`", example = "1", required = true)
            @PathVariable Long resumeUploadId) {
        return new ApiResponse<>(resumeService.parse(resumeUploadId));
    }
}
