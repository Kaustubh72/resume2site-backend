package com.resume2site.backend.resume.service.parser;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ResumeTextNormalizer {

    public String normalize(String rawText) {
        if (rawText == null || rawText.isBlank()) {
            return "";
        }

        String normalized = rawText
                .replace('\u00A0', ' ')
                .replace("\r\n", "\n")
                .replace('\r', '\n')
                .replaceAll("[\t\f\u000B]+", " ")
                .replaceAll("[ ]{2,}", " ")
                .replaceAll("\n{3,}", "\n\n")
                .replaceAll(" ?• ?", "\n- ")
                .trim();

        return Arrays.stream(normalized.split("\\n"))
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .reduce(new StringBuilder(), (builder, line) -> {
                    if (!builder.isEmpty()) {
                        builder.append('\n');
                    }
                    builder.append(line);
                    return builder;
                }, StringBuilder::append)
                .toString();
    }

    public List<String> toLines(String normalizedText) {
        if (normalizedText == null || normalizedText.isBlank()) {
            return List.of();
        }
        return Arrays.stream(normalizedText.split("\\n"))
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .toList();
    }
}
