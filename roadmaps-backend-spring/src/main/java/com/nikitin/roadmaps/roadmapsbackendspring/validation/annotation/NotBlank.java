package com.nikitin.roadmaps.roadmapsbackendspring.validation.annotation;

import com.nikitin.roadmaps.roadmapsbackendspring.validation.constraint.NotBlankConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NotBlankConstraint.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlank {

    boolean allowNull() default false;
    String message() default "Поле должно содержать текст";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
