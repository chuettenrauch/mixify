package com.github.chuettenrauch.mixifyapi.file.validation;

import com.github.chuettenrauch.mixifyapi.file.validation.validator.IsOwnedByAuthenticatedUserValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsOwnedByAuthenticatedUserValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsOwnedByAuthenticatedUser {
    String message() default "File must be owner by authenticated user.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
