package com.github.chuettenrauch.mixifyapi.mixtape.validation.validator;

import com.github.chuettenrauch.mixifyapi.mixtape.validation.SpotifyUri;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpotifyUriValidator implements ConstraintValidator<SpotifyUri, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        return value.matches("spotify:track:[\\w]+");
    }
}
