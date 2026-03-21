package com.resume2site.backend.resume;

import com.resume2site.backend.common.api.ApiResponse;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @GetMapping("/foundation-status")
    public ApiResponse<Map<String, String>> foundationStatus() {
        // TODO: Implement anonymous upload and parsing pipeline in the next module.
        return new ApiResponse<>(Map.of("status", "resume foundation ready"));
    }
}
