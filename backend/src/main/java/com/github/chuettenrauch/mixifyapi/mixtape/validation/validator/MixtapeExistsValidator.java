package com.github.chuettenrauch.mixifyapi.mixtape.validation.validator;

import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.mixtape.validation.MixtapeExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MixtapeExistsValidator implements ConstraintValidator<MixtapeExists, String> {

    private final MixtapeService mixtapeService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        return this.mixtapeService.existsById(value);
    }

}
