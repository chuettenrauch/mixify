package com.github.chuettenrauch.mixifyapi.mixtape.validation.validator;

import com.github.chuettenrauch.mixifyapi.mixtape.validation.ImageUrl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class ImageUrlValidator implements ConstraintValidator<ImageUrl, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return this.isAbsoluteUrl(value) || isRelativeUrlToUploadedFile(value);
    }

    private boolean isAbsoluteUrl(String value) {
        try {
            new URL(value).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }

        return true;
    }

    private boolean isRelativeUrlToUploadedFile(String value) {
        return value.matches("/api/files/[\\w]+");
    }
}
