package com.github.chuettenrauch.mixifyapi.unit.mixtape.validation.validator;

import com.github.chuettenrauch.mixifyapi.mixtape.validation.validator.ProviderUriValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class ProviderUriValidatorTest {

    @ParameterizedTest
    @CsvSource({
            "spotify:track:1231sdfdsf, true",
            "spotify:track:, false",
            "spotify:track, false",
            "something:spotify:track:1231sdfdsf, false",
            "spotify:track:1231sdfdsf:something, false",
            "some-random-string, false",
            ", true",
            "'', true",
    })
    void isValid(String value, boolean expected) {
        // given
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        // when
        ProviderUriValidator sut = new ProviderUriValidator();
        boolean actual = sut.isValid(value, context);

        // then
        assertEquals(expected, actual);
    }
}