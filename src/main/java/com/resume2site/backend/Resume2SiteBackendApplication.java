package com.resume2site.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Resume2SiteBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Resume2SiteBackendApplication.class, args);
    }
}
