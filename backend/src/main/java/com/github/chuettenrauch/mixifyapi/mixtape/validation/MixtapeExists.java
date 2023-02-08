package com.github.chuettenrauch.mixifyapi.mixtape.validation;

import com.github.chuettenrauch.mixifyapi.mixtape.validation.validator.MixtapeExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MixtapeExistsValidator.class)
@Documented
public @interface MixtapeExists {
    String message() default "mixtape does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}