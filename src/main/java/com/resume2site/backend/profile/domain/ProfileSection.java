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
@Schema(name = "ProfileSection", description = "Persisted section visibility/order configuration attached to a profile.")
@Table(name = "profile_sections")
public class ProfileSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key identifier", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @Schema(description = "Parent profile that owns this child row")
    private Profile profile;

    @Column(name = "section_key", nullable = false, length = 100)
    @Schema(description = "Stable logical section key", example = "projects")
    private String sectionKey;

    @Column(name = "display_name", nullable = false, length = 100)
    @Schema(description = "User-facing section label", example = "Projects")
    private String displayName;

    @Column(name = "is_visible", nullable = false)
    @Schema(description = "Whether the section is visible publicly", example = "true")
    private boolean visible;

    @Column(name = "sort_order", nullable = false)
    @Schema(description = "Sort order within the parent collection", example = "0")
    private Integer sortOrder;
}
