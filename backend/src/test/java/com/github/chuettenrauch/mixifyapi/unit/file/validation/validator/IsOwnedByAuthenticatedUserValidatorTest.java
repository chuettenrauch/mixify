package com.github.chuettenrauch.mixifyapi.unit.file.validation.validator;

import com.github.chuettenrauch.mixifyapi.file.exception.FileNotFoundException;
import com.github.chuettenrauch.mixifyapi.file.model.File;
import com.github.chuettenrauch.mixifyapi.file.service.FileService;
import com.github.chuettenrauch.mixifyapi.file.validation.validator.IsOwnedByAuthenticatedUserValidator;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IsOwnedByAuthenticatedUserValidatorTest {

    @Test
    void isValid_whenNotLoggedIn_thenReturnFalse() {
        // given
        FileService fileService = mock(FileService.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        // when
        IsOwnedByAuthenticatedUserValidator sut = new IsOwnedByAuthenticatedUserValidator(fileService, userService);
        boolean actual = sut.isValid("123", null);

        // then
        assertFalse(actual);
    }

    @Test
    void isValid_whenLoggedInButFileNotFound_thenReturnFalse() throws IOException {
        // given
        String fileId = "file-1";

        User authenticatedUser = new User();
        authenticatedUser.setId("123");

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(authenticatedUser));

        FileService fileService = mock(FileService.class);
        when(fileService.findFileByIdForUser(fileId, authenticatedUser)).thenThrow(FileNotFoundException.class);

        // when
        IsOwnedByAuthenticatedUserValidator sut = new IsOwnedByAuthenticatedUserValidator(fileService, userService);
        boolean actual = sut.isValid(fileId, null);

        // then
        assertFalse(actual);
    }

    @Test
    void isValid_whenLoggedInAndFileFound_thenReturnTrue() throws IOException {
        // given
        String fileId = "file-1";

        User authenticatedUser = new User();
        authenticatedUser.setId("123");

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(authenticatedUser));

        FileService fileService = mock(FileService.class);
        when(fileService.findFileByIdForUser(fileId, authenticatedUser)).thenReturn(mock(File.class));

        // when
        IsOwnedByAuthenticatedUserValidator sut = new IsOwnedByAuthenticatedUserValidator(fileService, userService);
        boolean actual = sut.isValid(fileId, null);

        // then
        assertTrue(actual);
    }

}