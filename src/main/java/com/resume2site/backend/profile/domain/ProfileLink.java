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
@Schema(name = "ProfileLink", description = "Persisted external link entry attached to a profile.")
@Table(name = "profile_links")
public class ProfileLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key identifier", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @Schema(description = "Parent profile that owns this child row")
    private Profile profile;

    @Column(name = "label", nullable = false, length = 100)
    @Schema(description = "Display label", example = "GitHub")
    private String label;

    @Column(name = "url", nullable = false, length = 500)
    @Schema(description = "External URL", example = "https://github.com/alice")
    private String url;

    @Column(name = "sort_order", nullable = false)
    @Schema(description = "Sort order within the parent collection", example = "0")
    private Integer sortOrder;
}
