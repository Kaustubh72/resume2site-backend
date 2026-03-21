package com.resume2site.backend.resume.service.parser;

import java.util.ArrayList;
import java.util.List;

public record ParsedProfileData(
        String fullName,
        String email,
        String phone,
        String professionalSummary,
        List<ParsedLink> links,
        List<ParsedSkill> skills,
        List<ParsedExperience> experiences,
        List<ParsedEducation> education,
        List<ParsedProject> projects
) {
    public ParsedProfileData {
        links = links == null ? new ArrayList<>() : links;
        skills = skills == null ? new ArrayList<>() : skills;
        experiences = experiences == null ? new ArrayList<>() : experiences;
        education = education == null ? new ArrayList<>() : education;
        projects = projects == null ? new ArrayList<>() : projects;
    }

    public record ParsedLink(String label, String url) {
    }

    public record ParsedSkill(String name, String category) {
    }

    public record ParsedExperience(String company, String title, String location, String description) {
    }

    public record ParsedEducation(String institution, String degree, String fieldOfStudy, String description) {
    }

    public record ParsedProject(String name, String description, String projectUrl, String repositoryUrl, String techStack) {
    }
}
