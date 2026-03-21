package com.resume2site.backend.profile.repository;

import com.resume2site.backend.profile.domain.ProfileExperience;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileExperienceRepository extends JpaRepository<ProfileExperience, Long> {
    List<ProfileExperience> findAllByProfileIdOrderBySortOrderAsc(Long profileId);
}
