package com.github.chuettenrauch.mixifyapi.unit.invite.service;

import com.github.chuettenrauch.mixifyapi.config.AppProperties;
import com.github.chuettenrauch.mixifyapi.exception.UnprocessableEntityException;
import com.github.chuettenrauch.mixifyapi.invite.model.Invite;
import com.github.chuettenrauch.mixifyapi.invite.repository.InviteRepository;
import com.github.chuettenrauch.mixifyapi.invite.service.InviteService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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

        AppProperties.Invite inviteProperties = mock(AppProperties.Invite.class);
        when(inviteProperties.getExpirationTime()).thenReturn("PT5H");

        AppProperties appProperties = mock(AppProperties.class);
        when(appProperties.getInvite()).thenReturn(inviteProperties);

        // when
        InviteService sut = new InviteService(inviteRepository, appProperties);

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
        AppProperties appProperties = mock(AppProperties.class);

        // when
        InviteService sut = new InviteService(inviteRepository, appProperties);
        assertThrows(UnprocessableEntityException.class, () -> sut.save(invite));

        // then
        verify(inviteRepository, never()).save(invite);
    }
}