package com.resume2site.backend.profile.repository;

import com.resume2site.backend.profile.domain.ProfileEducation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileEducationRepository extends JpaRepository<ProfileEducation, Long> {
}
