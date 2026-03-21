package com.resume2site.backend.resume.service.parser;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record ResumeSections(Map<String, List<String>> sections, List<String> orderedLines) {
    public ResumeSections {
        sections = sections == null ? Collections.emptyMap() : sections;
        orderedLines = orderedLines == null ? List.of() : orderedLines;
    }

    public List<String> get(String key) {
        return sections.getOrDefault(key, List.of());
    }
}
