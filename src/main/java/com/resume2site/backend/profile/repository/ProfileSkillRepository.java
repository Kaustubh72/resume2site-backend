package com.resume2site.backend.profile.repository;

import com.resume2site.backend.profile.domain.ProfileSkill;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileSkillRepository extends JpaRepository<ProfileSkill, Long> {
    List<ProfileSkill> findAllByProfileIdOrderBySortOrderAsc(Long profileId);
}
