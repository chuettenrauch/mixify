package com.github.chuettenrauch.mixifyapi.unit.mixtape_user.service;

import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.invite.model.Invite;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.mixtape_user.model.MixtapeUser;
import com.github.chuettenrauch.mixifyapi.mixtape_user.repository.MixtapeUserRepository;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MixtapeUserServiceTest {

    @Test
    void createFromInviteForAuthenticatedUserIfNotExists_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        Invite invite = new Invite();

        MixtapeUserRepository mixtapeUserRepository = mock(MixtapeUserRepository.class);
        MixtapeService mixtapeService = mock(MixtapeService.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        // when
        MixtapeUserService sut = new MixtapeUserService(mixtapeUserRepository, mixtapeService, userService);

        assertThrows(UnauthorizedException.class, () -> sut.createFromInviteForAuthenticatedUserIfNotExists(invite));

        // then
        verify(mixtapeUserRepository, never()).save(any());
    }

    @Test
    void createFromInviteForAuthenticatedUserIfNotExists_whenMixtapeDoesNotExist_thenThrowNotFoundException() {
        // given
        Invite invite = new Invite();
        invite.setMixtape("123");

        User user = new User();

        MixtapeUserRepository mixtapeUserRepository = mock(MixtapeUserRepository.class);

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findById(invite.getMixtape())).thenThrow(NotFoundException.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        // when
        MixtapeUserService sut = new MixtapeUserService(mixtapeUserRepository, mixtapeService, userService);

        assertThrows(NotFoundException.class, () -> sut.createFromInviteForAuthenticatedUserIfNotExists(invite));

        // then
        verify(mixtapeUserRepository, never()).save(any());
    }

    @Test
    void createFromInviteForAuthenticatedUserIfNotExists_whenLoggedInAndMixtapeNotExists_thenCreateMixtapeUser() {
        // given
        Mixtape mixtape = new Mixtape();
        mixtape.setId("123");

        Invite invite = new Invite();
        invite.setMixtape(mixtape.getId());

        User user = new User();

        MixtapeUser expected = new MixtapeUser(null, user, mixtape);

        MixtapeUserRepository mixtapeUserRepository = mock(MixtapeUserRepository.class);
        when(mixtapeUserRepository.findOneByUserAndMixtape(user, mixtape)).thenReturn(Optional.empty());
        when(mixtapeUserRepository.save(expected)).thenReturn(expected);

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findById(invite.getMixtape())).thenReturn(mixtape);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        // when
        MixtapeUserService sut = new MixtapeUserService(mixtapeUserRepository, mixtapeService, userService);
        MixtapeUser actual = sut.createFromInviteForAuthenticatedUserIfNotExists(invite);

        // then
        assertEquals(expected, actual);
        verify(mixtapeUserRepository).save(expected);
    }

    @Test
    void createFromInviteForAuthenticatedUserIfNotExists_whenMixtapeUserAlreadyExists_thenReturnMixtapeUser() {
        // given
        Mixtape mixtape = new Mixtape();
        mixtape.setId("123");

        Invite invite = new Invite();
        invite.setMixtape(mixtape.getId());

        User user = new User();

        MixtapeUser expected = new MixtapeUser(null, user, mixtape);

        MixtapeUserRepository mixtapeUserRepository = mock(MixtapeUserRepository.class);
        when(mixtapeUserRepository.findOneByUserAndMixtape(user, mixtape)).thenReturn(Optional.of(expected));
        when(mixtapeUserRepository.save(expected)).thenReturn(expected);

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findById(invite.getMixtape())).thenReturn(mixtape);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        // when
        MixtapeUserService sut = new MixtapeUserService(mixtapeUserRepository, mixtapeService, userService);
        MixtapeUser actual = sut.createFromInviteForAuthenticatedUserIfNotExists(invite);

        // then
        assertEquals(expected, actual);
        verify(mixtapeUserRepository).save(expected);
    }

}