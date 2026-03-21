package com.resume2site.backend.resume.service.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ResumeFieldExtractor {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_PATTERN = Pattern.compile("(?:\\+?\\d{1,3}[ -]?)?(?:\\(\\d{3}\\)|\\d{3})[ -]?\\d{3}[ -]?\\d{4}");
    private static final Pattern URL_PATTERN = Pattern.compile("(https?://[^\\s]+|www\\.[^\\s]+|(?:linkedin|github)\\.com/[^\\s]+)", Pattern.CASE_INSENSITIVE);
    private static final Set<String> SUMMARY_STOP_HEADERS = Set.of("skills", "experience", "education", "projects");

    private static final Map<String, List<String>> SKILL_KEYWORDS = Map.of(
            "Languages", List.of("java", "python", "javascript", "typescript", "c++", "c#", "go", "ruby", "kotlin", "sql"),
            "Frameworks", List.of("spring boot", "spring", "react", "node.js", "node", "express", "django", "flask", "hibernate", "junit"),
            "Tools", List.of("git", "docker", "kubernetes", "aws", "azure", "gcp", "postgresql", "mysql", "mongodb", "redis", "maven")
    );

    public Optional<String> extractFullName(List<String> lines) {
        return lines.stream()
                .limit(5)
                .map(this::cleanHeaderLine)
                .filter(this::looksLikeFullName)
                .findFirst();
    }

    public Optional<String> extractEmail(String text) {
        return firstMatch(text, EMAIL_PATTERN);
    }

    public Optional<String> extractPhone(String text) {
        return firstMatch(text, PHONE_PATTERN)
                .map(value -> value.replaceAll("\\s+", " ").trim());
    }

    public List<ParsedProfileData.ParsedLink> extractLinks(String text) {
        Matcher matcher = URL_PATTERN.matcher(text);
        Map<String, ParsedProfileData.ParsedLink> deduped = new LinkedHashMap<>();
        while (matcher.find()) {
            String rawUrl = matcher.group(1);
            String normalized = rawUrl.startsWith("http") ? rawUrl : "https://" + rawUrl;
            String lower = normalized.toLowerCase(Locale.ROOT);
            String label = lower.contains("linkedin.com") ? "LinkedIn"
                    : lower.contains("github.com") ? "GitHub"
                    : "Portfolio";
            deduped.putIfAbsent(lower, new ParsedProfileData.ParsedLink(label, normalized));
        }
        return new ArrayList<>(deduped.values());
    }

    public Optional<String> extractSummary(ResumeSections sections) {
        List<String> summaryLines = new ArrayList<>(sections.get("summary"));
        if (summaryLines.isEmpty()) {
            for (String line : sections.get("header")) {
                String lower = line.toLowerCase(Locale.ROOT);
                if (SUMMARY_STOP_HEADERS.stream().anyMatch(lower::contains)) {
                    break;
                }
                if (line.length() > 40) {
                    summaryLines.add(line);
                }
            }
        }
        if (summaryLines.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(String.join(" ", summaryLines).trim());
    }

    public List<ParsedProfileData.ParsedSkill> extractSkills(ResumeSections sections, String fullText) {
        Set<String> found = new LinkedHashSet<>();
        String lowerText = fullText.toLowerCase(Locale.ROOT);
        for (Map.Entry<String, List<String>> entry : SKILL_KEYWORDS.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (lowerText.contains(keyword)) {
                    found.add(entry.getKey() + "::" + keyword);
                }
            }
        }

        for (String line : sections.get("skills")) {
            for (String token : line.split("[,|•/]+")) {
                String normalized = token.trim();
                if (!normalized.isBlank() && normalized.length() <= 40) {
                    found.add("Other::" + normalized);
                }
            }
        }

        return found.stream()
                .map(entry -> {
                    String[] parts = entry.split("::", 2);
                    return new ParsedProfileData.ParsedSkill(titleCase(parts[1]), parts[0].equals("Other") ? null : parts[0]);
                })
                .toList();
    }

    public List<ParsedProfileData.ParsedExperience> extractExperience(ResumeSections sections) {
        return extractBlocks(sections.get("experience")).stream()
                .map(block -> {
                    String title = block.getFirst();
                    String company = block.size() > 1 ? block.get(1) : block.getFirst();
                    String description = block.size() > 2 ? String.join(" ", block.subList(2, block.size())) : null;
                    return new ParsedProfileData.ParsedExperience(cleanBullet(title), cleanBullet(company), null, description);
                })
                .filter(item -> !item.title().isBlank() && !item.company().isBlank())
                .toList();
    }

    public List<ParsedProfileData.ParsedEducation> extractEducation(ResumeSections sections) {
        return extractBlocks(sections.get("education")).stream()
                .map(block -> {
                    String institution = cleanBullet(block.getFirst());
                    String degree = block.size() > 1 ? cleanBullet(block.get(1)) : null;
                    String description = block.size() > 2 ? String.join(" ", block.subList(2, block.size())) : null;
                    return new ParsedProfileData.ParsedEducation(institution, degree, null, description);
                })
                .filter(item -> !item.institution().isBlank())
                .toList();
    }

    public List<ParsedProfileData.ParsedProject> extractProjects(ResumeSections sections) {
        return extractBlocks(sections.get("projects")).stream()
                .map(block -> {
                    String name = cleanBullet(block.getFirst());
                    String description = block.size() > 1 ? String.join(" ", block.subList(1, block.size())) : null;
                    String projectUrl = extractLinks(String.join(" ", block)).stream()
                            .map(ParsedProfileData.ParsedLink::url)
                            .findFirst()
                            .orElse(null);
                    String repositoryUrl = extractLinks(String.join(" ", block)).stream()
                            .map(ParsedProfileData.ParsedLink::url)
                            .filter(url -> url.toLowerCase(Locale.ROOT).contains("github.com"))
                            .findFirst()
                            .orElse(null);
                    return new ParsedProfileData.ParsedProject(name, description, projectUrl, repositoryUrl, null);
                })
                .filter(item -> !item.name().isBlank())
                .toList();
    }

    private Optional<String> firstMatch(String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? Optional.of(matcher.group()) : Optional.empty();
    }

    private List<List<String>> extractBlocks(List<String> sectionLines) {
        List<List<String>> blocks = new ArrayList<>();
        List<String> current = new ArrayList<>();
        for (String line : sectionLines) {
            if (looksLikeEntryBoundary(line, current) && !current.isEmpty()) {
                blocks.add(List.copyOf(current));
                current = new ArrayList<>();
            }
            current.add(line);
        }
        if (!current.isEmpty()) {
            blocks.add(List.copyOf(current));
        }
        return blocks;
    }

    private boolean looksLikeEntryBoundary(String line, List<String> current) {
        String cleaned = cleanBullet(line);
        return current.size() >= 2 && !cleaned.isBlank() && !line.startsWith("-") && cleaned.length() < 120;
    }

    private String cleanHeaderLine(String line) {
        return line.replaceAll("\\|.*$", "").trim();
    }

    private boolean looksLikeFullName(String line) {
        if (line.isBlank() || line.length() > 60 || line.matches(".*\\d.*")) {
            return false;
        }
        String[] parts = line.split(" ");
        if (parts.length < 2 || parts.length > 4) {
            return false;
        }
        return java.util.Arrays.stream(parts)
                .allMatch(part -> Character.isUpperCase(part.charAt(0)));
    }

    private String cleanBullet(String value) {
        return value.replaceFirst("^[-•*]\\s*", "").trim();
    }

    private String titleCase(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        if (value.equalsIgnoreCase("aws") || value.equalsIgnoreCase("gcp") || value.equalsIgnoreCase("sql") || value.equalsIgnoreCase("c#") || value.equalsIgnoreCase("c++")) {
            return value.toUpperCase(Locale.ROOT);
        }
        String[] parts = value.split(" ");
        List<String> converted = new ArrayList<>();
        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }
            converted.add(Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase(Locale.ROOT));
        }
        return String.join(" ", converted);
    }
}
