package com.resume2site.backend.profile.domain;

import com.resume2site.backend.resume.domain.ResumeUpload;
import com.resume2site.backend.shared.domain.BaseEntity;
import com.resume2site.backend.template.domain.Template;
import com.resume2site.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_upload_id")
    private ResumeUpload resumeUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    @Column(name = "draft_token", unique = true, length = 100)
    private String draftToken;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(length = 255)
    private String headline;

    @Column(length = 255)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(length = 255)
    private String location;

    @Column(name = "professional_summary", columnDefinition = "TEXT")
    private String professionalSummary;

    @Column(length = 100)
    private String slug;

    @Column(name = "publication_status", nullable = false, length = 30)
    private String publicationStatus;

    @Column(name = "published_at")
    private java.time.Instant publishedAt;
}
