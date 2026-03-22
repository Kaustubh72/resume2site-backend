package com.resume2site.backend.resume.service;

import com.resume2site.backend.common.exception.ConflictException;
import com.resume2site.backend.resume.ResumeConstants;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

@Service
public class ResumeTextExtractionService {

    private final Tika tika = new Tika();

    public String extract(Path filePath) {
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            return tika.parseToString(inputStream);
        } catch (Exception exception) {
            throw new ConflictException(ResumeConstants.MESSAGE_EXTRACT_TEXT_FAILED);
        }
    }
}
