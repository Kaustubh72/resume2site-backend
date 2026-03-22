package com.resume2site.backend.profile.domain;

import com.resume2site.backend.resume.domain.ResumeUpload;
import com.resume2site.backend.shared.domain.BaseEntity;
import com.resume2site.backend.template.domain.Template;
import com.resume2site.backend.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Schema(name = "Profile", description = "Persisted structured profile root entity. Stores ownership, publication state, template selection, and top-level editable fields.")
@Table(name = "profiles")
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key identifier", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Schema(description = "Owning user once the draft is attached to an authenticated account")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_upload_id")
    @Schema(description = "Source resume upload from which the draft profile was initially generated")
    private ResumeUpload resumeUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    @Schema(description = "Selected frontend template used to render the profile")
    private Template template;

    @Column(name = "draft_token", unique = true, length = 100)
    @Schema(description = "Anonymous draft token used before the profile is attached to a logged-in user", example = "high-entropy-draft-token")
    private String draftToken;

    @Column(name = "full_name", length = 255)
    @Schema(description = "Profile full name", example = "Alice Johnson")
    private String fullName;

    @Column(length = 255)
    @Schema(description = "Profile headline", example = "Software Engineer")
    private String headline;

    @Column(length = 255)
    @Schema(description = "Profile email", example = "alice@example.com")
    private String email;

    @Column(length = 50)
    @Schema(description = "Profile phone number", example = "+1-555-0100")
    private String phone;

    @Column(length = 255)
    @Schema(description = "Profile location string", example = "New York, NY")
    private String location;

    @Column(name = "professional_summary", columnDefinition = "TEXT")
    @Schema(description = "Professional summary/about text")
    private String professionalSummary;

    @Column(length = 100)
    @Schema(description = "Public slug once published", example = "alice-johnson")
    private String slug;

    @Column(name = "publication_status", nullable = false, length = 30)
    @Schema(description = "Publication status", example = "DRAFT")
    private String publicationStatus;

    @Column(name = "published_at")
    @Schema(description = "Last published timestamp", example = "2026-03-22T10:15:30Z")
    private java.time.Instant publishedAt;
}
