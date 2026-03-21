package com.resume2site.backend.resume.service;

import com.resume2site.backend.common.exception.BadRequestException;
import com.resume2site.backend.common.exception.ResourceNotFoundException;
import com.resume2site.backend.config.UploadProperties;
import com.resume2site.backend.profile.domain.Profile;
import com.resume2site.backend.profile.domain.ProfileEducation;
import com.resume2site.backend.profile.domain.ProfileExperience;
import com.resume2site.backend.profile.domain.ProfileLink;
import com.resume2site.backend.profile.domain.ProfileProject;
import com.resume2site.backend.profile.domain.ProfileSkill;
import com.resume2site.backend.profile.dto.ProfileSummaryResponse;
import com.resume2site.backend.profile.repository.ProfileEducationRepository;
import com.resume2site.backend.profile.repository.ProfileExperienceRepository;
import com.resume2site.backend.profile.repository.ProfileLinkRepository;
import com.resume2site.backend.profile.repository.ProfileProjectRepository;
import com.resume2site.backend.profile.repository.ProfileRepository;
import com.resume2site.backend.profile.repository.ProfileSkillRepository;
import com.resume2site.backend.resume.domain.ResumeUpload;
import com.resume2site.backend.resume.dto.ResumeParseResponse;
import com.resume2site.backend.resume.dto.ResumeUploadResponse;
import com.resume2site.backend.resume.repository.ResumeUploadRepository;
import com.resume2site.backend.resume.service.parser.ParsedProfileData;
import com.resume2site.backend.resume.service.parser.ResumeProfileParser;
import jakarta.transaction.Transactional;
import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeService {

    private final UploadProperties uploadProperties;
    private final ResumeStorageService resumeStorageService;
    private final ResumeTextExtractionService textExtractionService;
    private final ResumeProfileParser profileParser;
    private final ResumeUploadRepository resumeUploadRepository;
    private final ProfileRepository profileRepository;
    private final ProfileLinkRepository profileLinkRepository;
    private final ProfileSkillRepository profileSkillRepository;
    private final ProfileExperienceRepository profileExperienceRepository;
    private final ProfileEducationRepository profileEducationRepository;
    private final ProfileProjectRepository profileProjectRepository;

    public ResumeService(UploadProperties uploadProperties,
                         ResumeStorageService resumeStorageService,
                         ResumeTextExtractionService textExtractionService,
                         ResumeProfileParser profileParser,
                         ResumeUploadRepository resumeUploadRepository,
                         ProfileRepository profileRepository,
                         ProfileLinkRepository profileLinkRepository,
                         ProfileSkillRepository profileSkillRepository,
                         ProfileExperienceRepository profileExperienceRepository,
                         ProfileEducationRepository profileEducationRepository,
                         ProfileProjectRepository profileProjectRepository) {
        this.uploadProperties = uploadProperties;
        this.resumeStorageService = resumeStorageService;
        this.textExtractionService = textExtractionService;
        this.profileParser = profileParser;
        this.resumeUploadRepository = resumeUploadRepository;
        this.profileRepository = profileRepository;
        this.profileLinkRepository = profileLinkRepository;
        this.profileSkillRepository = profileSkillRepository;
        this.profileExperienceRepository = profileExperienceRepository;
        this.profileEducationRepository = profileEducationRepository;
        this.profileProjectRepository = profileProjectRepository;
    }

    @Transactional
    public ResumeUploadResponse upload(MultipartFile file) {
        validateUpload(file);
        String extension = getExtension(file.getOriginalFilename());
        Path storedPath = resumeStorageService.store(file, extension);

        ResumeUpload upload = new ResumeUpload();
        upload.setOriginalFileName(file.getOriginalFilename());
        upload.setContentType(file.getContentType());
        upload.setFileSizeBytes(file.getSize());
        upload.setStoragePath(storedPath.toString());
        upload.setParseStatus("UPLOADED");

        ResumeUpload saved = resumeUploadRepository.save(upload);
        return new ResumeUploadResponse(saved.getId(), saved.getOriginalFileName(), saved.getContentType(), saved.getFileSizeBytes(), saved.getParseStatus());
    }

    @Transactional
    public ResumeParseResponse parse(Long resumeUploadId) {
        ResumeUpload upload = resumeUploadRepository.findById(resumeUploadId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume upload not found"));

        if ("PARSED".equalsIgnoreCase(upload.getParseStatus())) {
            Profile existingProfile = profileRepository.findByResumeUploadId(resumeUploadId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parsed profile not found for resume upload"));
            return new ResumeParseResponse(upload.getId(), upload.getParseStatus(), toProfileSummary(existingProfile));
        }

        Path filePath = Path.of(upload.getStoragePath());
        try {
            upload.setParseStatus("PARSING");
            String extractedText = textExtractionService.extract(filePath);
            upload.setExtractedText(extractedText);

            ParsedProfileData parsedProfileData = profileParser.parse(extractedText);

            Profile profile = new Profile();
            profile.setResumeUpload(upload);
            profile.setDraftToken(UUID.randomUUID().toString());
            profile.setFullName(parsedProfileData.fullName());
            profile.setEmail(parsedProfileData.email());
            profile.setPhone(parsedProfileData.phone());
            profile.setProfessionalSummary(parsedProfileData.professionalSummary());
            profile.setPublicationStatus("DRAFT");
            Profile savedProfile = profileRepository.save(profile);

            saveChildRecords(savedProfile, parsedProfileData);

            upload.setParseStatus("PARSED");
            resumeUploadRepository.save(upload);
            return new ResumeParseResponse(upload.getId(), upload.getParseStatus(), toProfileSummary(savedProfile));
        } catch (RuntimeException exception) {
            upload.setParseStatus("FAILED");
            throw exception;
        } finally {
            resumeStorageService.deleteIfExists(filePath);
        }
    }

    private void saveChildRecords(Profile profile, ParsedProfileData data) {
        for (int index = 0; index < data.links().size(); index++) {
            ParsedProfileData.ParsedLink item = data.links().get(index);
            ProfileLink link = new ProfileLink();
            link.setProfile(profile);
            link.setLabel(item.label());
            link.setUrl(item.url());
            link.setSortOrder(index);
            profileLinkRepository.save(link);
        }

        for (int index = 0; index < data.skills().size(); index++) {
            ParsedProfileData.ParsedSkill item = data.skills().get(index);
            ProfileSkill skill = new ProfileSkill();
            skill.setProfile(profile);
            skill.setName(item.name());
            skill.setCategory(item.category());
            skill.setSortOrder(index);
            profileSkillRepository.save(skill);
        }

        for (int index = 0; index < data.experiences().size(); index++) {
            ParsedProfileData.ParsedExperience item = data.experiences().get(index);
            ProfileExperience experience = new ProfileExperience();
            experience.setProfile(profile);
            experience.setTitle(item.title());
            experience.setCompany(item.company());
            experience.setLocation(item.location());
            experience.setDescription(item.description());
            experience.setSortOrder(index);
            profileExperienceRepository.save(experience);
        }

        for (int index = 0; index < data.education().size(); index++) {
            ParsedProfileData.ParsedEducation item = data.education().get(index);
            ProfileEducation education = new ProfileEducation();
            education.setProfile(profile);
            education.setInstitution(item.institution());
            education.setDegree(item.degree());
            education.setFieldOfStudy(item.fieldOfStudy());
            education.setDescription(item.description());
            education.setSortOrder(index);
            profileEducationRepository.save(education);
        }

        for (int index = 0; index < data.projects().size(); index++) {
            ParsedProfileData.ParsedProject item = data.projects().get(index);
            ProfileProject project = new ProfileProject();
            project.setProfile(profile);
            project.setName(item.name());
            project.setDescription(item.description());
            project.setProjectUrl(item.projectUrl());
            project.setRepositoryUrl(item.repositoryUrl());
            project.setTechStack(item.techStack());
            project.setSortOrder(index);
            profileProjectRepository.save(project);
        }
    }

    private void validateUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Resume file is required");
        }
        if (file.getSize() > uploadProperties.maxFileSizeBytes()) {
            throw new BadRequestException("Resume file exceeds the allowed size limit");
        }
        String extension = getExtension(file.getOriginalFilename());
        if (!uploadProperties.allowedExtensions().contains(extension)) {
            throw new BadRequestException("Only PDF and DOCX resume uploads are supported");
        }
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!uploadProperties.allowedContentTypes().contains(contentType)) {
            throw new BadRequestException("Uploaded file type is not supported");
        }
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new BadRequestException("Resume file must include a valid extension");
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }

    private ProfileSummaryResponse toProfileSummary(Profile profile) {
        return new ProfileSummaryResponse(
                profile.getId(),
                profile.getDraftToken(),
                profile.getFullName(),
                profile.getHeadline(),
                profile.getPublicationStatus(),
                profile.getSlug(),
                profile.getTemplate() != null ? profile.getTemplate().getId() : null
        );
    }
}
