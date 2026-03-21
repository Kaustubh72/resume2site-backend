package com.resume2site.backend.resume.service.parser;

import org.springframework.stereotype.Service;

@Service
public class ResumeProfileParser {

    private final ResumeTextNormalizer textNormalizer;
    private final ResumeSectionDetector sectionDetector;
    private final ResumeFieldExtractor fieldExtractor;

    public ResumeProfileParser(ResumeTextNormalizer textNormalizer,
                               ResumeSectionDetector sectionDetector,
                               ResumeFieldExtractor fieldExtractor) {
        this.textNormalizer = textNormalizer;
        this.sectionDetector = sectionDetector;
        this.fieldExtractor = fieldExtractor;
    }

    public ParsedProfileData parse(String rawText) {
        String normalizedText = textNormalizer.normalize(rawText);
        var lines = textNormalizer.toLines(normalizedText);
        var sections = sectionDetector.detectSections(lines);

        // TODO: Improve heuristics with scoring once real resume samples are available.
        return new ParsedProfileData(
                fieldExtractor.extractFullName(lines).orElse(null),
                fieldExtractor.extractEmail(normalizedText).orElse(null),
                fieldExtractor.extractPhone(normalizedText).orElse(null),
                fieldExtractor.extractSummary(sections).orElse(null),
                fieldExtractor.extractLinks(normalizedText),
                fieldExtractor.extractSkills(sections, normalizedText),
                fieldExtractor.extractExperience(sections),
                fieldExtractor.extractEducation(sections),
                fieldExtractor.extractProjects(sections)
        );
    }
}
