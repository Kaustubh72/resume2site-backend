package com.resume2site.backend.resume.service.parser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ResumeSectionDetector {

    private static final Map<String, Set<String>> SECTION_ALIASES = Map.of(
            "summary", Set.of("summary", "professional summary", "objective", "profile", "about"),
            "skills", Set.of("skills", "technical skills", "core skills", "technologies", "tech stack"),
            "experience", Set.of("experience", "work experience", "professional experience", "employment", "internships"),
            "education", Set.of("education", "academic background", "academics"),
            "projects", Set.of("projects", "personal projects", "academic projects", "key projects"),
            "links", Set.of("links", "profiles", "online presence")
    );

    public ResumeSections detectSections(List<String> lines) {
        Map<String, List<String>> sections = new LinkedHashMap<>();
        String currentSection = "header";
        sections.put(currentSection, new java.util.ArrayList<>());

        for (String line : lines) {
            String sectionKey = toSectionKey(line);
            if (sectionKey != null) {
                currentSection = sectionKey;
                sections.putIfAbsent(currentSection, new java.util.ArrayList<>());
                continue;
            }
            sections.computeIfAbsent(currentSection, ignored -> new java.util.ArrayList<>()).add(line);
        }

        return new ResumeSections(sections, lines);
    }

    private String toSectionKey(String line) {
        String candidate = line.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z ]", "")
                .trim();

        if (candidate.isBlank() || candidate.length() > 40) {
            return null;
        }

        for (Map.Entry<String, Set<String>> entry : SECTION_ALIASES.entrySet()) {
            if (entry.getValue().contains(candidate)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
