package com.resume2site.backend.template.domain;

import com.resume2site.backend.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "templates")
public class Template extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "preview_image_url", length = 500)
    private String previewImageUrl;

    @Column(length = 100)
    private String category;

    @Column(name = "accent_color", length = 30)
    private String accentColor;

    @Column(name = "features", columnDefinition = "TEXT")
    private String features;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}
