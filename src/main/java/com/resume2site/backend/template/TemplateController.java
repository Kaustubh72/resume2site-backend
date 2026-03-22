package com.resume2site.backend.template;

import com.resume2site.backend.common.api.ApiErrorResponse;
import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.template.dto.TemplateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/templates")
@Tag(name = "Templates", description = "Template catalog endpoints used by the frontend template picker and public renderer.")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Operation(summary = "List active templates", description = "Returns all active portfolio templates in display order for the template picker.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Templates returned", content = @Content(schema = @Schema(implementation = TemplateResponse.class)))
    })
    @GetMapping
    public ApiResponse<List<TemplateResponse>> listTemplates() {
        return new ApiResponse<>(templateService.listActiveTemplates());
    }

    @Operation(summary = "Get one active template", description = "Returns one active template by id for deeper template preview or selection details.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Template returned", content = @Content(schema = @Schema(implementation = TemplateResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Template not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{templateId}")
    public ApiResponse<TemplateResponse> getTemplate(
            @Parameter(description = "Template id", example = "2", required = true)
            @PathVariable Long templateId) {
        return new ApiResponse<>(templateService.getActiveTemplate(templateId));
    }
}
