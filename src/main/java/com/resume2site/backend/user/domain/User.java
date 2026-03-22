package com.resume2site.backend.user.domain;

import com.resume2site.backend.shared.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@Schema(name = "User", description = "Persisted Resume2Site user entity. Stores account-level ownership information for published profiles and resume uploads.")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User id", example = "1")
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    @Schema(description = "Normalized unique email", example = "alice@example.com")
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    @Schema(description = "BCrypt password hash. Persisted field only, never returned by public API.", accessMode = Schema.AccessMode.READ_ONLY)
    private String passwordHash;

    @Column(name = "full_name", length = 255)
    @Schema(description = "Full display name", example = "Alice Johnson")
    private String fullName;

    @Column(nullable = false, length = 30)
    @Schema(description = "Account status", example = "ACTIVE")
    private String status;
}
