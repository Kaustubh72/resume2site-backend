package com.resume2site.backend.profile.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "profile_sections")
public class ProfileSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name = "section_key", nullable = false, length = 100)
    private String sectionKey;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "is_visible", nullable = false)
    private boolean visible;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}
