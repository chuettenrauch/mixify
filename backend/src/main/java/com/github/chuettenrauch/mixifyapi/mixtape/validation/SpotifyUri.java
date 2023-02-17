package com.github.chuettenrauch.mixifyapi.mixtape.validation;

import com.github.chuettenrauch.mixifyapi.mixtape.validation.validator.SpotifyUriValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SpotifyUriValidator.class)
@Documented
public @interface SpotifyUri {
    String message() default "not a valid spotify uri";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
