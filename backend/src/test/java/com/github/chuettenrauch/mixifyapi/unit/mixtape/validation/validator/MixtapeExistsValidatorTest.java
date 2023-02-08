package com.github.chuettenrauch.mixifyapi.unit.mixtape.validation.validator;

import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.mixtape.validation.validator.MixtapeExistsValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MixtapeExistsValidatorTest {

    @Test
    void isValid_whenMixtapeExists_thenReturnTrue() {
        // given
        String mixtapeId = "abc123";

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.existsById(mixtapeId)).thenReturn(true);

        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        // when
        MixtapeExistsValidator sut = new MixtapeExistsValidator(mixtapeService);
        boolean actual = sut.isValid(mixtapeId, context);

        // then
        assertTrue(actual);
    }

    @Test
    void isValid_whenMixtapeDoesNotExist_thenReturnFalse() {
        // given
        String mixtapeId = "abc123";

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.existsById(mixtapeId)).thenReturn(false);

        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        // when
        MixtapeExistsValidator sut = new MixtapeExistsValidator(mixtapeService);
        boolean actual = sut.isValid(mixtapeId, context);

        // then
        assertFalse(actual);
    }

    @Test
    void isValid_whenMixtapeBlank_thenReturnTrue() {
        // given
        MixtapeService mixtapeService = mock(MixtapeService.class);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        // when
        MixtapeExistsValidator sut = new MixtapeExistsValidator(mixtapeService);

        assertTrue(sut.isValid("", context));
        assertTrue(sut.isValid(null, context));
    }
}