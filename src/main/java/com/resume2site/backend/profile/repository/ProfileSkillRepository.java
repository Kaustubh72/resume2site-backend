package com.resume2site.backend.profile.repository;

import com.resume2site.backend.profile.domain.ProfileSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileSkillRepository extends JpaRepository<ProfileSkill, Long> {
}
