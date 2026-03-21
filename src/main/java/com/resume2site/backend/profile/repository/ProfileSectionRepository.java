package com.resume2site.backend.profile.repository;

import com.resume2site.backend.profile.domain.ProfileSection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileSectionRepository extends JpaRepository<ProfileSection, Long> {
    List<ProfileSection> findAllByProfileIdOrderBySortOrderAsc(Long profileId);
}
