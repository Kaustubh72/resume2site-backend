package com.resume2site.backend.resume.service.parser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResumeProfileParserTest {

    private ResumeTextNormalizer textNormalizer;
    private ResumeSectionDetector sectionDetector;
    private ResumeFieldExtractor fieldExtractor;
    private ResumeProfileParser parser;

    @BeforeEach
    void setUp() {
        textNormalizer = new ResumeTextNormalizer();
        sectionDetector = new ResumeSectionDetector();
        fieldExtractor = new ResumeFieldExtractor();
        parser = new ResumeProfileParser(textNormalizer, sectionDetector, fieldExtractor);
    }

    @Test
    void normalizeCollapsesWhitespaceAndBullets() {
        String raw = "Alice Johnson\r\n\r\nSummary\r\n  Software engineer\u00A0with Java\t\tSpring\r\n• Built APIs\r\n• Shipped features";

        String normalized = textNormalizer.normalize(raw);

        assertThat(normalized).contains("Alice Johnson");
        assertThat(normalized).contains("Software engineer with Java Spring");
        assertThat(normalized).contains("- Built APIs");
        assertThat(normalized).doesNotContain("\r");
    }

    @Test
    void parserExtractsCoreFieldsFromTypicalDeveloperResume() {
        String resume = """
                Alice Johnson
                alice@example.com | +1 (555) 123-4567 | linkedin.com/in/alicejohnson | github.com/alice
                Summary
                Early-career software engineer building Spring Boot and React applications.
                Skills
                Java, Spring Boot, React, PostgreSQL, Docker, AWS
                Experience
                Software Engineer Intern
                Acme Corp
                - Built internal APIs for student onboarding
                Projects
                Resume2Site
                Portfolio platform that converts resumes into personal websites github.com/alice/resume2site
                Education
                State University
                B.S. Computer Science
                """;

        ParsedProfileData parsed = parser.parse(resume);

        assertThat(parsed.fullName()).isEqualTo("Alice Johnson");
        assertThat(parsed.email()).isEqualTo("alice@example.com");
        assertThat(parsed.phone()).contains("555");
        assertThat(parsed.professionalSummary()).contains("software engineer");
        assertThat(parsed.links()).extracting(ParsedProfileData.ParsedLink::label)
                .contains("LinkedIn", "GitHub");
        assertThat(parsed.skills()).extracting(ParsedProfileData.ParsedSkill::name)
                .contains("Java", "Spring Boot", "React", "Postgresql", "Docker", "AWS");
        assertThat(parsed.experiences()).hasSize(1);
        assertThat(parsed.education()).hasSize(1);
        assertThat(parsed.projects()).hasSize(1);
    }
}
