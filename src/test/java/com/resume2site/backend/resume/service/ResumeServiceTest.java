package com.resume2site.backend.resume.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.resume2site.backend.common.exception.BadRequestException;
import com.resume2site.backend.config.UploadProperties;
import com.resume2site.backend.profile.DraftTokenService;
import com.resume2site.backend.profile.domain.Profile;
import com.resume2site.backend.profile.repository.ProfileEducationRepository;
import com.resume2site.backend.profile.repository.ProfileExperienceRepository;
import com.resume2site.backend.profile.repository.ProfileLinkRepository;
import com.resume2site.backend.profile.repository.ProfileProjectRepository;
import com.resume2site.backend.profile.repository.ProfileRepository;
import com.resume2site.backend.profile.repository.ProfileSkillRepository;
import com.resume2site.backend.resume.domain.ResumeUpload;
import com.resume2site.backend.resume.repository.ResumeUploadRepository;
import com.resume2site.backend.resume.service.parser.ParsedProfileData;
import com.resume2site.backend.resume.service.parser.ResumeProfileParser;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class ResumeServiceTest {

    @Mock private ResumeStorageService resumeStorageService;
    @Mock private ResumeTextExtractionService textExtractionService;
    @Mock private ResumeProfileParser profileParser;
    @Mock private ResumeUploadRepository resumeUploadRepository;
    @Mock private ProfileRepository profileRepository;
    @Mock private ProfileLinkRepository profileLinkRepository;
    @Mock private ProfileSkillRepository profileSkillRepository;
    @Mock private ProfileExperienceRepository profileExperienceRepository;
    @Mock private ProfileEducationRepository profileEducationRepository;
    @Mock private ProfileProjectRepository profileProjectRepository;
    @Mock private DraftTokenService draftTokenService;

    private ResumeService resumeService;

    @BeforeEach
    void setUp() {
        UploadProperties uploadProperties = new UploadProperties(
                10_485_760L,
                List.of("application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
                List.of("pdf", "docx")
        );
        resumeService = new ResumeService(
                uploadProperties,
                resumeStorageService,
                textExtractionService,
                profileParser,
                resumeUploadRepository,
                profileRepository,
                profileLinkRepository,
                profileSkillRepository,
                profileExperienceRepository,
                profileEducationRepository,
                profileProjectRepository,
                draftTokenService
        );
    }

    @Test
    void uploadAllowsOctetStreamWhenExtensionIsSupported() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/octet-stream",
                "test-pdf".getBytes()
        );
        Path storedPath = Path.of("/tmp/resume.pdf");
        when(resumeStorageService.store(file, "pdf")).thenReturn(storedPath);
        when(resumeUploadRepository.save(any(ResumeUpload.class))).thenAnswer(invocation -> {
            ResumeUpload upload = invocation.getArgument(0);
            upload.setId(1L);
            return upload;
        });

        var response = resumeService.upload(file);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.contentType()).isEqualTo("application/octet-stream");
    }

    @Test
    void uploadRejectsUnsupportedExtension() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.exe",
                "application/octet-stream",
                "malware".getBytes()
        );

        assertThatThrownBy(() -> resumeService.upload(file))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Only PDF and DOCX resume uploads are supported");
    }

    @Test
    void parseFallsBackToEmptyDraftWhenParserThrows() {
        ResumeUpload upload = new ResumeUpload();
        upload.setId(9L);
        upload.setStoragePath("/tmp/resume.pdf");
        upload.setParseStatus("UPLOADED");

        when(resumeUploadRepository.findById(9L)).thenReturn(Optional.of(upload));
        when(textExtractionService.extract(Path.of("/tmp/resume.pdf"))).thenReturn("Alice Johnson\nalice@example.com");
        when(profileParser.parse(any())).thenThrow(new IllegalStateException("parser failed"));
        when(profileRepository.findByResumeUploadId(9L)).thenReturn(Optional.empty());
        when(draftTokenService.generate()).thenReturn("secure-token");
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> {
            Profile profile = invocation.getArgument(0);
            profile.setId(55L);
            return profile;
        });

        var response = resumeService.parse(9L);

        assertThat(response.parseStatus()).isEqualTo("PARSED");
        assertThat(response.profile().draftToken()).isEqualTo("secure-token");
        assertThat(response.profile().id()).isEqualTo(55L);
        verify(resumeStorageService).deleteIfExists(Path.of("/tmp/resume.pdf"));
    }

    @Test
    void parseRejectsBlankExtractedText() {
        ResumeUpload upload = new ResumeUpload();
        upload.setId(9L);
        upload.setStoragePath("/tmp/resume.pdf");
        upload.setParseStatus("UPLOADED");

        when(resumeUploadRepository.findById(9L)).thenReturn(Optional.of(upload));
        when(textExtractionService.extract(Path.of("/tmp/resume.pdf"))).thenReturn("   ");

        assertThatThrownBy(() -> resumeService.parse(9L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Uploaded resume did not contain readable text");

        verify(profileRepository, never()).save(any(Profile.class));
        verify(resumeStorageService).deleteIfExists(Path.of("/tmp/resume.pdf"));
    }
}
