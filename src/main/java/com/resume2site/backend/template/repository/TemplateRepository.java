package com.resume2site.backend.template.repository;

import com.resume2site.backend.template.domain.Template;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    List<Template> findAllByActiveTrueOrderBySortOrderAsc();
}
