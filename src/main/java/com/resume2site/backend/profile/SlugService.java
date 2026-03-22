package com.resume2site.backend.profile;

import com.resume2site.backend.common.exception.BadRequestException;
import com.resume2site.backend.profile.dto.SlugAvailabilityResponse;
import com.resume2site.backend.profile.repository.ProfileRepository;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class SlugService {

    private final ProfileRepository profileRepository;

    public SlugService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public SlugAvailabilityResponse checkAvailability(String rawValue) {
        String slug = SlugRules.normalize(rawValue);
        SlugRules.ValidationResult validation = SlugRules.validate(slug);
        if (!validation.valid()) {
            return new SlugAvailabilityResponse(slug, false, false, validation.message(), suggestAlternatives(slug, null));
        }

        boolean available = !profileRepository.existsBySlugIgnoreCase(slug);
        String message = available ? "Slug is available" : "Slug is already taken";
        List<String> suggestions = available ? List.of() : suggestAlternatives(slug, null);
        return new SlugAvailabilityResponse(slug, true, available, message, suggestions);
    }

    public String requireUsableSlug(String rawValue, Long currentProfileId) {
        String slug = SlugRules.normalize(rawValue);
        SlugRules.ValidationResult validation = SlugRules.validate(slug);
        if (!validation.valid()) {
            throw new BadRequestException(validation.message());
        }

        boolean exists = currentProfileId == null
                ? profileRepository.existsBySlugIgnoreCase(slug)
                : profileRepository.existsBySlugIgnoreCaseAndIdNot(slug, currentProfileId);
        if (exists) {
            throw new BadRequestException("Slug is already taken");
        }
        return slug;
    }

    public List<String> suggestionsFor(String rawValue, Long currentProfileId) {
        return suggestAlternatives(SlugRules.normalize(rawValue), currentProfileId);
    }

    private List<String> suggestAlternatives(String slug, Long currentProfileId) {
        String base = SlugRules.sanitizeBase(slug);
        if (base.length() < SlugRules.MIN_LENGTH) {
            base = (base + "site").substring(0, Math.min(SlugRules.MAX_LENGTH, Math.max(SlugRules.MIN_LENGTH, base.length() + 4)));
        }

        Set<String> suggestions = new LinkedHashSet<>();
        addIfAvailable(suggestions, base, currentProfileId);
        addIfAvailable(suggestions, SlugRules.trimToMax(base + "-site"), currentProfileId);
        addIfAvailable(suggestions, SlugRules.trimToMax(base + "-portfolio"), currentProfileId);
        for (int index = 2; suggestions.size() < 5 && index <= 20; index++) {
            addIfAvailable(suggestions, SlugRules.trimToMax(base + "-" + index), currentProfileId);
        }
        return suggestions.stream().limit(5).toList();
    }

    private void addIfAvailable(Set<String> suggestions, String candidate, Long currentProfileId) {
        SlugRules.ValidationResult validation = SlugRules.validate(candidate);
        if (!validation.valid()) {
            return;
        }

        boolean exists = currentProfileId == null
                ? profileRepository.existsBySlugIgnoreCase(candidate)
                : profileRepository.existsBySlugIgnoreCaseAndIdNot(candidate, currentProfileId);
        if (!exists) {
            suggestions.add(candidate);
        }
    }
}
