package com.resume2site.backend.resume.domain;

import com.resume2site.backend.shared.domain.BaseEntity;
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
@Schema(name = "ResumeUpload", description = "Persisted resume upload record used for anonymous upload, parse tracking, and later draft ownership handoff.")
@Table(name = "resume_uploads")
public class ResumeUpload extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key identifier", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Schema(description = "Owning user once the draft is attached to an authenticated account")
    private User user;

    @Column(name = "original_file_name", nullable = false)
    @Schema(description = "Original uploaded file name", example = "resume.pdf")
    private String originalFileName;

    @Column(name = "content_type", nullable = false, length = 150)
    @Schema(description = "Stored MIME type", example = "application/pdf")
    private String contentType;

    @Column(name = "storage_path", nullable = false)
    @Schema(description = "Temporary storage path on the backend host", accessMode = Schema.AccessMode.READ_ONLY)
    private String storagePath;

    @Column(name = "file_size_bytes", nullable = false)
    @Schema(description = "Uploaded file size in bytes", example = "245678")
    private Long fileSizeBytes;

    @Column(name = "parse_status", nullable = false, length = 30)
    @Schema(description = "Resume parse lifecycle status", example = "UPLOADED")
    private String parseStatus;

    @Column(name = "extracted_text", columnDefinition = "TEXT")
    @Schema(description = "Best-effort extracted resume text used for parsing", accessMode = Schema.AccessMode.READ_ONLY)
    private String extractedText;
}
