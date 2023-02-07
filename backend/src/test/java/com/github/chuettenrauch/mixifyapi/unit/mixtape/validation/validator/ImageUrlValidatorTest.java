package com.github.chuettenrauch.mixifyapi.unit.mixtape.validation.validator;

import com.github.chuettenrauch.mixifyapi.mixtape.validation.validator.ImageUrlValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ImageUrlValidatorTest {

    @ParameterizedTest
    @CsvSource({
            "/api/files/123, true",
            "http://some-url.de, true",
            "http://some-url.de/with/path, true",
            "http://some-url.de/with/path?and=query&params=1, true",
            "http://some-url.de/with/path?and=query&params=1#and-hash, true",
            "https://some-url.de, true",
            "https://some-url.de/with/path?and=query&params=1, true",
            "https://some-url.de/with/path?and=query&params=1#and-hash, true",
            "/api/files, false",
            "/api/files/, false",
            "some/path/with/api/files/123/in/it, false",
            "/some/other/relative/url, false",
            "some-random-string, false",
            ", false",
    })
    void isValid(String value, boolean expected) {
        // given
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        // when
        ImageUrlValidator sut = new ImageUrlValidator();
        boolean actual = sut.isValid(value, context);

        // then
        assertEquals(expected, actual);
    }
}