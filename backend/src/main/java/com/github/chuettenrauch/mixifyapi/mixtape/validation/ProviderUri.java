package com.github.chuettenrauch.mixifyapi.mixtape.validation;

import com.github.chuettenrauch.mixifyapi.mixtape.validation.validator.ProviderUriValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProviderUriValidator.class)
@Documented
public @interface ProviderUri {
    String message() default "not a valid provider uri";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
