package com.resume2site.backend.resume.service.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ResumeFieldExtractorTest {

    private final ResumeFieldExtractor extractor = new ResumeFieldExtractor();

    @Test
    void extractLinksDeduplicatesAndLabelsKnownPlatforms() {
        var links = extractor.extractLinks("linkedin.com/in/alice github.com/alice https://github.com/alice");

        assertThat(links)
                .extracting(ParsedProfileData.ParsedLink::label)
                .containsExactly("LinkedIn", "GitHub");
    }

    @Test
    void extractSummaryFallsBackToHeaderNarrativeWhenSummarySectionMissing() {
        ResumeSections sections = new ResumeSections(
                java.util.Map.of("header", List.of(
                        "Backend engineer focused on Spring Boot and PostgreSQL systems.",
                        "Skills"
                )),
                List.of()
        );

        assertThat(extractor.extractSummary(sections))
                .contains("Backend engineer focused on Spring Boot and PostgreSQL systems.");
    }
}
