package com.nikitin.roadmaps.roadmapsbackendspring.validation.constraint;

import com.nikitin.roadmaps.roadmapsbackendspring.validation.annotation.NotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class NotBlankConstraint implements ConstraintValidator<NotBlank, String> {

    private boolean allowNull;

    @Override
    public void initialize(NotBlank constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (allowNull) {
            return StringUtils.hasText(value) || value == null;
        }
        return StringUtils.hasText(value);
    }
}
