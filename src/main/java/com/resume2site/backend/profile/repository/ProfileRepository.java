package com.resume2site.backend.profile.repository;

import com.resume2site.backend.profile.domain.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByDraftToken(String draftToken);
    Optional<Profile> findByResumeUploadId(Long resumeUploadId);
    Optional<Profile> findBySlugAndPublicationStatus(String slug, String publicationStatus);
    boolean existsBySlugIgnoreCase(String slug);
    boolean existsBySlugIgnoreCaseAndIdNot(String slug, Long id);
}
