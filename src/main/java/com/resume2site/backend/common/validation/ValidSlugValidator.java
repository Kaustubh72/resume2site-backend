package com.resume2site.backend.common.validation;

import com.resume2site.backend.profile.SlugRules;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidSlugValidator implements ConstraintValidator<ValidSlug, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        SlugRules.ValidationResult validation = SlugRules.validate(value);
        if (validation.valid()) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(validation.message()).addConstraintViolation();
        return false;
    }
}
