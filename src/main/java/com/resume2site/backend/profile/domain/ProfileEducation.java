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
@Schema(name = "ProfileEducation", description = "Persisted education entry attached to a profile.")
@Table(name = "profile_education")
public class ProfileEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key identifier", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @Schema(description = "Parent profile that owns this child row")
    private Profile profile;

    @Column(name = "institution", nullable = false, length = 255)
    @Schema(description = "Institution name", example = "State University")
    private String institution;

    @Column(name = "degree", length = 255)
    @Schema(description = "Degree/program", example = "B.S. Computer Science")
    private String degree;

    @Column(name = "field_of_study", length = 255)
    @Schema(description = "Field of study", example = "Computer Science")
    private String fieldOfStudy;

    @Column(name = "start_date")
    @Schema(description = "Start date", example = "2024-01-01")
    private LocalDate startDate;

    @Column(name = "end_date")
    @Schema(description = "End date", example = "2025-01-01")
    private LocalDate endDate;

    @Column(name = "grade", length = 100)
    @Schema(description = "Optional grade/GPA/honors note", example = "3.8 GPA")
    private String grade;

    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(description = "Description/details for this row")
    private String description;

    @Column(name = "sort_order", nullable = false)
    @Schema(description = "Sort order within the parent collection", example = "0")
    private Integer sortOrder;
}
