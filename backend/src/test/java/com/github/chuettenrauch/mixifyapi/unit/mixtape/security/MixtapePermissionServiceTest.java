package com.github.chuettenrauch.mixifyapi.unit.mixtape.security;

import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.security.MixtapePermissionService;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MixtapePermissionServiceTest {

    @Test
    void canView_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        String mixtapeId = "123";

        MixtapeService mixtapeService = mock(MixtapeService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        // when + then
        MixtapePermissionService sut = new MixtapePermissionService(mixtapeService, mixtapeUserService, userService);

        assertThrows(UnauthorizedException.class, () -> sut.canView(mixtapeId));
    }

    @Test
    void canView_whenMixtapeUserEntryExistsForUserAndMixtape_thenReturnTrue() {
        // given
        User user = new User();

        Mixtape mixtape = new Mixtape();
        mixtape.setId("123");

        MixtapeService mixtapeService = mock(MixtapeService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        when(mixtapeUserService.existsByUserAndMixtape(user, mixtape)).thenReturn(true);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        // when
        MixtapePermissionService sut = new MixtapePermissionService(mixtapeService, mixtapeUserService, userService);
        boolean actual = sut.canView(mixtape.getId());

        // then
        assertTrue(actual);
    }

    @Test
    void canView_whenMixtapeUserEntryDoesNotExistForUserAndMixtape_thenReturnFalse() {
        // given
        User user = new User();

        Mixtape mixtape = new Mixtape();
        mixtape.setId("123");

        MixtapeService mixtapeService = mock(MixtapeService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        when(mixtapeUserService.existsByUserAndMixtape(user, mixtape)).thenReturn(false);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        // when
        MixtapePermissionService sut = new MixtapePermissionService(mixtapeService, mixtapeUserService, userService);
        boolean actual = sut.canView(mixtape.getId());

        // then
        assertFalse(actual);
    }

    @Test
    void canEdit_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        String mixtapeId = "123";

        MixtapeService mixtapeService = mock(MixtapeService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        // when + then
        MixtapePermissionService sut = new MixtapePermissionService(mixtapeService, mixtapeUserService, userService);

        assertThrows(UnauthorizedException.class, () -> sut.canEdit(mixtapeId));
    }

    @Test
    void canEdit_whenUserIsCreatorOfMixtape_thenReturnTrue() {
        // given
        String mixtapeId = "123";
        User user = new User();

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.existsByIdAndCreatedByAndDraftTrue(mixtapeId, user)).thenReturn(true);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        // when
        MixtapePermissionService sut = new MixtapePermissionService(mixtapeService, mixtapeUserService, userService);
        boolean actual = sut.canEdit(mixtapeId);

        // then
        assertTrue(actual);
    }


    @Test
    void canEdit_whenUserIsNotCreatorOfMixtape_thenReturnFalse() {
        // given
        String mixtapeId = "123";
        User user = new User();

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.existsByIdAndCreatedByAndDraftTrue(mixtapeId, user)).thenReturn(false);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        // when
        MixtapePermissionService sut = new MixtapePermissionService(mixtapeService, mixtapeUserService, userService);
        boolean actual = sut.canEdit(mixtapeId);

        // then
        assertFalse(actual);
    }

    @Test
    void canDelete_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        String mixtapeId = "123";

        MixtapeService mixtapeService = mock(MixtapeService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        // when + then
        MixtapePermissionService sut = new MixtapePermissionService(mixtapeService, mixtapeUserService, userService);

        assertThrows(UnauthorizedException.class, () -> sut.canDelete(mixtapeId));
    }

    @Test
    void canDelete_whenMixtapeUserEntryExistsForUserAndMixtape_thenReturnTrue() {
        // given
        User user = new User();

        Mixtape mixtape = new Mixtape();
        mixtape.setId("123");

        MixtapeService mixtapeService = mock(MixtapeService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        when(mixtapeUserService.existsByUserAndMixtape(user, mixtape)).thenReturn(true);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        // when
        MixtapePermissionService sut = new MixtapePermissionService(mixtapeService, mixtapeUserService, userService);
        boolean actual = sut.canDelete(mixtape.getId());

        // then
        assertTrue(actual);
    }

    @Test
    void canDelete_whenMixtapeUserEntryDoesNotExistForUserAndMixtape_thenReturnFalse() {
        // given
        User user = new User();

        Mixtape mixtape = new Mixtape();
        mixtape.setId("123");

        MixtapeService mixtapeService = mock(MixtapeService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        when(mixtapeUserService.existsByUserAndMixtape(user, mixtape)).thenReturn(false);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        // when
        MixtapePermissionService sut = new MixtapePermissionService(mixtapeService, mixtapeUserService, userService);
        boolean actual = sut.canDelete(mixtape.getId());

        // then
        assertFalse(actual);
    }

}