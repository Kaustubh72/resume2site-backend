package com.resume2site.backend.template;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.resume2site.backend.common.exception.ResourceNotFoundException;
import com.resume2site.backend.template.domain.Template;
import com.resume2site.backend.template.dto.TemplateResponse;
import com.resume2site.backend.template.repository.TemplateRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

    @Mock private TemplateRepository templateRepository;

    @InjectMocks private TemplateService templateService;

    @Test
    void listActiveTemplatesSplitsFeatureMetadata() {
        Template template = template();
        when(templateRepository.findAllByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(template));

        List<TemplateResponse> responses = templateService.listActiveTemplates();

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().features()).containsExactly("Single-column layout", "Readable projects");
    }

    @Test
    void getActiveTemplateRejectsMissingTemplate() {
        when(templateRepository.findByIdAndActiveTrue(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> templateService.getActiveTemplate(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Template not found");
    }

    private Template template() {
        Template template = new Template();
        template.setId(1L);
        template.setCode("minimal-dev");
        template.setName("Minimal Developer");
        template.setDescription("Clean layout");
        template.setPreviewImageUrl("https://cdn.resume2site.dev/templates/minimal-dev.png");
        template.setCategory("developer");
        template.setAccentColor("#111827");
        template.setFeatures("Single-column layout|Readable projects");
        template.setSortOrder(1);
        template.setActive(true);
        return template;
    }
}
