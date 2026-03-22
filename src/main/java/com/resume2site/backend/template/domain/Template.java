package com.resume2site.backend.template.domain;

import com.resume2site.backend.shared.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Schema(name = "Template", description = "Persisted template entity that stores frontend rendering metadata for one active/inactive portfolio template.")
@Table(name = "templates")
public class Template extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key identifier", example = "1")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @Schema(description = "Stable frontend template code", example = "minimal-dev")
    private String code;

    @Column(nullable = false, length = 120)
    @Schema(description = "Name/title value for this row", example = "Spring Boot")
    private String name;

    @Column(length = 500)
    @Schema(description = "Description/details for this row")
    private String description;

    @Column(name = "preview_image_url", length = 500)
    @Schema(description = "Template preview image URL")
    private String previewImageUrl;

    @Column(length = 100)
    @Schema(description = "Optional category label", example = "Frameworks")
    private String category;

    @Column(name = "accent_color", length = 30)
    @Schema(description = "Primary accent color", example = "#111827")
    private String accentColor;

    @Column(name = "features", columnDefinition = "TEXT")
    @Schema(description = "Pipe-delimited feature metadata persisted in the database")
    private String features;

    @Column(nullable = false)
    @Schema(description = "Whether the template is available for selection", example = "true")
    private boolean active;

    @Column(name = "sort_order", nullable = false)
    @Schema(description = "Sort order within the parent collection", example = "0")
    private Integer sortOrder;
}
