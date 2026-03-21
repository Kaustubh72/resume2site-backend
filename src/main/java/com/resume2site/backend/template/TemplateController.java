package com.resume2site.backend.template;

import com.resume2site.backend.common.api.ApiResponse;
import com.resume2site.backend.template.dto.TemplateResponse;
import com.resume2site.backend.template.repository.TemplateRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/templates")
@Tag(name = "Templates", description = "Template management operations")
public class TemplateController {

    private final TemplateRepository templateRepository;

    public TemplateController(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Operation(summary = "List all active templates")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Templates retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ApiResponse<List<TemplateResponse>> listTemplates() {
        List<TemplateResponse> templates = templateRepository.findAllByActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(template -> new TemplateResponse(
                        template.getId(),
                        template.getCode(),
                        template.getName(),
                        template.getDescription(),
                        template.getPreviewImageUrl(),
                        template.getSortOrder()
                ))
                .toList();
        return new ApiResponse<>(templates);
    }
}
