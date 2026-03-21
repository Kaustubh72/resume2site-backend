package com.resume2site.backend.template;

import com.resume2site.backend.common.exception.ResourceNotFoundException;
import com.resume2site.backend.template.domain.Template;
import com.resume2site.backend.template.dto.TemplateResponse;
import com.resume2site.backend.template.repository.TemplateRepository;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Transactional(readOnly = true)
    public List<TemplateResponse> listActiveTemplates() {
        return templateRepository.findAllByActiveTrueOrderBySortOrderAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TemplateResponse getActiveTemplate(Long templateId) {
        Template template = templateRepository.findByIdAndActiveTrue(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        return toResponse(template);
    }

    private TemplateResponse toResponse(Template template) {
        return new TemplateResponse(
                template.getId(),
                template.getCode(),
                template.getName(),
                template.getDescription(),
                template.getPreviewImageUrl(),
                template.getCategory(),
                template.getAccentColor(),
                splitFeatures(template.getFeatures()),
                template.getSortOrder()
        );
    }

    private List<String> splitFeatures(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Stream.of(value.split("\\|"))
                .map(String::trim)
                .filter(feature -> !feature.isEmpty())
                .toList();
    }
}
