package com.resume2site.backend.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.upload")
public record UploadProperties(
        long maxFileSizeBytes,
        List<String> allowedContentTypes,
        List<String> allowedExtensions
) {
    public UploadProperties {
        allowedContentTypes = allowedContentTypes == null ? new ArrayList<>() : allowedContentTypes;
        allowedExtensions = allowedExtensions == null ? new ArrayList<>() : allowedExtensions;
    }
}
