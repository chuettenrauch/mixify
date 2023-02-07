package com.github.chuettenrauch.mixifyapi.mixtape.validation;

import com.github.chuettenrauch.mixifyapi.mixtape.validation.validator.ImageUrlValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageUrlValidator.class)
@Documented
public @interface ImageUrl {
    String message() default "must be an absolute URL or URL to uploaded file";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
