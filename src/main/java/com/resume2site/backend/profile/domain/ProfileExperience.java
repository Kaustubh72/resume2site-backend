package com.resume2site.backend.profile.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Schema(name = "ProfileExperience", description = "Persisted work experience entry attached to a profile.")
@Table(name = "profile_experiences")
public class ProfileExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key identifier", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @Schema(description = "Parent profile that owns this child row")
    private Profile profile;

    @Column(name = "company", nullable = false, length = 255)
    @Schema(description = "Company/organization", example = "Acme Corp")
    private String company;

    @Column(name = "title", nullable = false, length = 255)
    @Schema(description = "Role title", example = "Software Engineer")
    private String title;

    @Column(name = "location", length = 255)
    @Schema(description = "Profile location string", example = "New York, NY")
    private String location;

    @Column(name = "start_date")
    @Schema(description = "Start date", example = "2024-01-01")
    private LocalDate startDate;

    @Column(name = "end_date")
    @Schema(description = "End date", example = "2025-01-01")
    private LocalDate endDate;

    @Column(name = "is_current", nullable = false)
    @Schema(description = "Whether the role is ongoing", example = "true")
    private boolean current;

    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(description = "Description/details for this row")
    private String description;

    @Column(name = "sort_order", nullable = false)
    @Schema(description = "Sort order within the parent collection", example = "0")
    private Integer sortOrder;
}
