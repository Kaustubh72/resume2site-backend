package com.resume2site.backend.profile.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Schema(name = "ProfileProject", description = "Persisted project entry attached to a profile.")
@Table(name = "profile_projects")
public class ProfileProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key identifier", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @Schema(description = "Parent profile that owns this child row")
    private Profile profile;

    @Column(name = "name", nullable = false, length = 255)
    @Schema(description = "Name/title value for this row", example = "Spring Boot")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(description = "Description/details for this row")
    private String description;

    @Column(name = "project_url", length = 500)
    @Schema(description = "Optional live demo URL", example = "https://resume2site.dev")
    private String projectUrl;

    @Column(name = "repository_url", length = 500)
    @Schema(description = "Optional source repository URL", example = "https://github.com/alice/resume2site")
    private String repositoryUrl;

    @Column(name = "tech_stack", columnDefinition = "TEXT")
    @Schema(description = "Free-form tech stack summary", example = "Java, Spring Boot, React")
    private String techStack;

    @Column(name = "sort_order", nullable = false)
    @Schema(description = "Sort order within the parent collection", example = "0")
    private Integer sortOrder;
}
