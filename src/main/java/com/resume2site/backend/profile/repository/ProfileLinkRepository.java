package com.resume2site.backend.profile.repository;

import com.resume2site.backend.profile.domain.ProfileLink;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileLinkRepository extends JpaRepository<ProfileLink, Long> {
    List<ProfileLink> findAllByProfileIdOrderBySortOrderAsc(Long profileId);
}
