package com.github.chuettenrauch.mixifyapi.unit.invite.service;

import com.github.chuettenrauch.mixifyapi.config.AppProperties;
import com.github.chuettenrauch.mixifyapi.exception.GoneException;
import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.exception.UnprocessableEntityException;
import com.github.chuettenrauch.mixifyapi.invite.model.Invite;
import com.github.chuettenrauch.mixifyapi.invite.repository.InviteRepository;
import com.github.chuettenrauch.mixifyapi.invite.service.InviteService;
import com.github.chuettenrauch.mixifyapi.mixtape_user.model.MixtapeUser;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
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

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        AppProperties.Invite inviteProperties = mock(AppProperties.Invite.class);
        when(inviteProperties.getExpirationTime()).thenReturn("PT5H");

        AppProperties appProperties = mock(AppProperties.class);
        when(appProperties.getInvite()).thenReturn(inviteProperties);

        // when
        InviteService sut = new InviteService(inviteRepository, mixtapeUserService, appProperties);

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
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        AppProperties appProperties = mock(AppProperties.class);

        // when
        InviteService sut = new InviteService(inviteRepository, mixtapeUserService, appProperties);
        assertThrows(UnprocessableEntityException.class, () -> sut.save(invite));

        // then
        verify(inviteRepository, never()).save(invite);
    }

    @Test
    void acceptInviteByIdForAuthenticatedUser_whenInviteDoesNotExist_thenThrowNotFoundException() {
        // given
        String id = "123";

        InviteRepository inviteRepository = mock(InviteRepository.class);
        when(inviteRepository.findById(id)).thenReturn(Optional.empty());

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        AppProperties appProperties = mock(AppProperties.class);

        // when
        InviteService sut = new InviteService(inviteRepository, mixtapeUserService, appProperties);

        assertThrows(NotFoundException.class, () -> sut.acceptInviteByIdForAuthenticatedUser(id));

        // then
        verify(mixtapeUserService, never()).createFromInviteForAuthenticatedUser(any());
    }

    @Test
    void acceptInviteByIdForAuthenticatedUser_whenInviteIsExpired_thenThrowGoneException() {
        // given
        String id = "123";

        Invite invite = mock(Invite.class);
        when(invite.isExpired()).thenReturn(true);

        InviteRepository inviteRepository = mock(InviteRepository.class);
        when(inviteRepository.findById(id)).thenReturn(Optional.of(invite));

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        AppProperties appProperties = mock(AppProperties.class);

        // when
        InviteService sut = new InviteService(inviteRepository, mixtapeUserService, appProperties);

        assertThrows(GoneException.class, () -> sut.acceptInviteByIdForAuthenticatedUser(id));

        // then
        verify(mixtapeUserService, never()).createFromInviteForAuthenticatedUser(any());
    }

    @Test
    void acceptInviteByIdForAuthenticatedUser_whenInviteIsValidButMixtapeDoesNotExist_thenThrowGoneException() {
        // given
        String id = "123";

        Invite invite = mock(Invite.class);
        when(invite.isExpired()).thenReturn(true);

        InviteRepository inviteRepository = mock(InviteRepository.class);
        when(inviteRepository.findById(id)).thenReturn(Optional.of(invite));

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        when(mixtapeUserService.createFromInviteForAuthenticatedUser(invite)).thenThrow(NotFoundException.class);

        AppProperties appProperties = mock(AppProperties.class);

        // when
        InviteService sut = new InviteService(inviteRepository, mixtapeUserService, appProperties);

        assertThrows(GoneException.class, () -> sut.acceptInviteByIdForAuthenticatedUser(id));
    }

    @Test
    void acceptInviteByIdForAuthenticatedUser_whenLoggedInAndInviteStillValid_thenCreateAndReturnMixtapeUser() {
        // given
        String id = "123";

        Invite invite = mock(Invite.class);
        when(invite.isExpired()).thenReturn(false);

        MixtapeUser expected = new MixtapeUser();

        InviteRepository inviteRepository = mock(InviteRepository.class);
        when(inviteRepository.findById(id)).thenReturn(Optional.of(invite));

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        when(mixtapeUserService.createFromInviteForAuthenticatedUser(invite)).thenReturn(expected);

        AppProperties appProperties = mock(AppProperties.class);

        // when
        InviteService sut = new InviteService(inviteRepository, mixtapeUserService, appProperties);
        MixtapeUser actual = sut.acceptInviteByIdForAuthenticatedUser(id);

        // then
        assertEquals(expected, actual);
        verify(mixtapeUserService).createFromInviteForAuthenticatedUser(invite);
    }

}