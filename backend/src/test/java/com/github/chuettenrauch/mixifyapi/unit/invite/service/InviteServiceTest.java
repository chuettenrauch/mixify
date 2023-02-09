package com.github.chuettenrauch.mixifyapi.unit.invite.service;

import com.github.chuettenrauch.mixifyapi.config.AppProperties;
import com.github.chuettenrauch.mixifyapi.exception.GoneException;
import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.exception.UnprocessableEntityException;
import com.github.chuettenrauch.mixifyapi.invite.model.Invite;
import com.github.chuettenrauch.mixifyapi.invite.repository.InviteRepository;
import com.github.chuettenrauch.mixifyapi.invite.service.InviteService;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.mixtape_user.model.MixtapeUser;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

class InviteServiceTest {

    @Test
    void save_whenCalled_thenDelegateToInviteRepository() {
        // given
        Invite given = new Invite();
        given.setMixtape("abc123");

        InviteRepository inviteRepository = mock(InviteRepository.class);
        when(inviteRepository.save(given)).thenReturn(given);

        UserService userService = mock(UserService.class);
        MixtapeService mixtapeService = mock(MixtapeService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        AppProperties.Invite inviteProperties = mock(AppProperties.Invite.class);
        when(inviteProperties.getExpirationTime()).thenReturn("PT5H");

        AppProperties appProperties = mock(AppProperties.class);
        when(appProperties.getInvite()).thenReturn(inviteProperties);

        // when
        InviteService sut = new InviteService(
                inviteRepository,
                userService,
                mixtapeService,
                mixtapeUserService,
                appProperties
        );

        LocalDateTime beforeCreate = LocalDateTime.now();
        Invite actual = sut.save(given);
        LocalDateTime afterCreate = LocalDateTime.now();

        // then
        assertEquals(given.getMixtape(), actual.getMixtape());
        assertThat(actual.getExpiredAt())
                .isAfter(beforeCreate.plusHours(5))
                .isBefore(afterCreate.plusHours(5));

        verify(inviteRepository).save(any());
    }

    @Test
    void save_whenInviteHasId_thenThrowUnprocessableEntityException() {
        // given
        Invite invite = new Invite();
        invite.setId("123");
        invite.setMixtape("abc123");

        InviteRepository inviteRepository = mock(InviteRepository.class);
        UserService userService = mock(UserService.class);
        MixtapeService mixtapeService = mock(MixtapeService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        AppProperties appProperties = mock(AppProperties.class);

        // when
        InviteService sut = new InviteService(
                inviteRepository,
                userService,
                mixtapeService,
                mixtapeUserService,
                appProperties
        );

        assertThrows(UnprocessableEntityException.class, () -> sut.save(invite));

        // then
        verify(inviteRepository, never()).save(invite);
    }

    @Test
    void acceptInviteByIdForAuthenticatedUser_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        String id = "123";

        InviteRepository inviteRepository = mock(InviteRepository.class);
        when(inviteRepository.findById(id)).thenReturn(Optional.empty());

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        MixtapeService mixtapeService = mock(MixtapeService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        AppProperties appProperties = mock(AppProperties.class);

        // when
        InviteService sut = new InviteService(
                inviteRepository,
                userService,
                mixtapeService,
                mixtapeUserService,
                appProperties
        );

        assertThrows(UnauthorizedException.class, () -> sut.acceptInviteByIdForAuthenticatedUser(id));

        // then
        verify(mixtapeUserService, never()).createIfNotExists(any(), any());
    }

    @Test
    void acceptInviteByIdForAuthenticatedUser_whenInviteDoesNotExist_thenThrowNotFoundException() {
        // given
        String id = "123";
        User user = new User();

        InviteRepository inviteRepository = mock(InviteRepository.class);
        when(inviteRepository.findById(id)).thenReturn(Optional.empty());

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeService mixtapeService = mock(MixtapeService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        AppProperties appProperties = mock(AppProperties.class);

        // when
        InviteService sut = new InviteService(
                inviteRepository,
                userService,
                mixtapeService,
                mixtapeUserService,
                appProperties
        );

        assertThrows(NotFoundException.class, () -> sut.acceptInviteByIdForAuthenticatedUser(id));

        // then
        verify(mixtapeUserService, never()).createIfNotExists(any(), any());
    }

    @Test
    void acceptInviteByIdForAuthenticatedUser_whenInviteIsExpired_thenThrowGoneException() {
        // given
        String id = "123";

        User user = new User();

        Invite invite = mock(Invite.class);
        when(invite.isExpired()).thenReturn(true);

        InviteRepository inviteRepository = mock(InviteRepository.class);
        when(inviteRepository.findById(id)).thenReturn(Optional.of(invite));

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeService mixtapeService = mock(MixtapeService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        AppProperties appProperties = mock(AppProperties.class);

        // when
        InviteService sut = new InviteService(
                inviteRepository,
                userService,
                mixtapeService,
                mixtapeUserService,
                appProperties
        );

        assertThrows(GoneException.class, () -> sut.acceptInviteByIdForAuthenticatedUser(id));

        // then
        verify(mixtapeUserService, never()).createIfNotExists(any(), any());
    }

    @Test
    void acceptInviteByIdForAuthenticatedUser_whenInviteIsValidButMixtapeDoesNotExist_thenThrowGoneException() {
        // given
        String id = "123";

        User user = new User();

        Invite invite = mock(Invite.class);
        when(invite.isExpired()).thenReturn(true);

        InviteRepository inviteRepository = mock(InviteRepository.class);
        when(inviteRepository.findById(id)).thenReturn(Optional.of(invite));

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findById(id)).thenThrow(NotFoundException.class);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        AppProperties appProperties = mock(AppProperties.class);

        // when
        InviteService sut = new InviteService(
                inviteRepository,
                userService,
                mixtapeService,
                mixtapeUserService,
                appProperties
        );

        assertThrows(GoneException.class, () -> sut.acceptInviteByIdForAuthenticatedUser(id));

        // then
        verify(mixtapeUserService, never()).createIfNotExists(any(), any());
    }

    @Test
    void acceptInviteByIdForAuthenticatedUser_whenLoggedInAndInviteStillValid_thenCreateAndReturnMixtapeUser() {
        // given
        String id = "123";

        User user = new User();
        Mixtape mixtape = new Mixtape();
        mixtape.setId("mixtape-123");

        Invite invite = mock(Invite.class);
        when(invite.isExpired()).thenReturn(false);
        when(invite.getMixtape()).thenReturn(mixtape.getId());

        MixtapeUser expected = new MixtapeUser();

        InviteRepository inviteRepository = mock(InviteRepository.class);
        when(inviteRepository.findById(id)).thenReturn(Optional.of(invite));

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findById(mixtape.getId())).thenReturn(mixtape);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        when(mixtapeUserService.createIfNotExists(user, mixtape)).thenReturn(expected);

        AppProperties appProperties = mock(AppProperties.class);

        // when
        InviteService sut = new InviteService(
                inviteRepository,
                userService,
                mixtapeService,
                mixtapeUserService,
                appProperties
        );

        MixtapeUser actual = sut.acceptInviteByIdForAuthenticatedUser(id);

        // then
        assertEquals(expected, actual);
        verify(mixtapeUserService).createIfNotExists(user, mixtape);
    }

}