package com.github.chuettenrauch.mixifyapi.mixtape.validation.validator;

import com.github.chuettenrauch.mixifyapi.mixtape.validation.ProviderUri;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProviderUriValidator implements ConstraintValidator<ProviderUri, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && value.matches("spotify:track:[\\w]+");
    }
}
