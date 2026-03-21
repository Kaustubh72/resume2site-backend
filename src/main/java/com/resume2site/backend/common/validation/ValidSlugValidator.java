package com.resume2site.backend.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidSlugValidator implements ConstraintValidator<ValidSlug, String> {

    private static final String PATTERN = "^[a-z0-9]+(?:-[a-z0-9]+)*$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.matches(PATTERN);
    }
}
