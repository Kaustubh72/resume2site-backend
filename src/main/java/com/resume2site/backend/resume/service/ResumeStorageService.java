package com.resume2site.backend.resume.service;

import com.resume2site.backend.common.exception.BadRequestException;
import com.resume2site.backend.resume.ResumeConstants;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeStorageService {

    public Path store(MultipartFile file, String extension) {
        try {
            Path directory = Files.createTempDirectory(ResumeConstants.TEMP_DIRECTORY_PREFIX);
            Path target = directory.resolve(UUID.randomUUID() + "." + extension.toLowerCase(Locale.ROOT));
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return target;
        } catch (IOException exception) {
            throw new BadRequestException(ResumeConstants.MESSAGE_STORE_UPLOAD_FAILED);
        }
    }

    public void deleteIfExists(Path path) {
        if (path == null) {
            return;
        }
        try {
            Files.deleteIfExists(path);
            Path parent = path.getParent();
            if (parent != null) {
                Files.deleteIfExists(parent);
            }
        } catch (IOException ignored) {
            // Best-effort cleanup for temporary resume storage.
        }
    }
}
