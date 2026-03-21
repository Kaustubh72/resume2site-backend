package com.resume2site.backend.profile.repository;

import com.resume2site.backend.profile.domain.ProfileProject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileProjectRepository extends JpaRepository<ProfileProject, Long> {
    List<ProfileProject> findAllByProfileIdOrderBySortOrderAsc(Long profileId);
}
