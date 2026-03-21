package com.resume2site.backend.template;

import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.template.dto.TemplateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/templates")
@Tag(name = "Templates", description = "Template management operations")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Operation(summary = "List all active templates")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Templates retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ApiResponse<List<TemplateResponse>> listTemplates() {
        return new ApiResponse<>(templateService.listActiveTemplates());
    }

    @Operation(summary = "Get one active template by id")
    @GetMapping("/{templateId}")
    public ApiResponse<TemplateResponse> getTemplate(@PathVariable Long templateId) {
        return new ApiResponse<>(templateService.getActiveTemplate(templateId));
    }
}
