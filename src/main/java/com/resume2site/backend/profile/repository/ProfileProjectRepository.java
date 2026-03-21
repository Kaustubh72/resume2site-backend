package com.resume2site.backend.profile.repository;

import com.resume2site.backend.profile.domain.ProfileProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileProjectRepository extends JpaRepository<ProfileProject, Long> {
}
