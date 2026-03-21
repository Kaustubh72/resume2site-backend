package com.resume2site.backend.resume.repository;

import com.resume2site.backend.resume.domain.ResumeUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeUploadRepository extends JpaRepository<ResumeUpload, Long> {
}
