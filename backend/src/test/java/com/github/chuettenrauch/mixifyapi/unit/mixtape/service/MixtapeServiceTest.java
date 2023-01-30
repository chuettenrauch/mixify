package com.github.chuettenrauch.mixifyapi.unit.mixtape.service;

import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.mixtape.exception.MixtapeNotFoundException;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MixtapeServiceTest {

    @Test
    void save_whenCalled_thenDelegateToMixtapeRepository() {
        // given
        Mixtape expected = new Mixtape();

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.save(expected)).thenReturn(expected);

        UserService userService = mock(UserService.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService);
        Mixtape actual = sut.save(expected);

        // then
        assertEquals(expected, actual);
        verify(mixtapeRepository).save(expected);
    }

    @Test
    void findAllForAuthenticatedUser_whenLoggedIn_thenDelegateToMixtapeRepository() {
        // given
        User user = new User();
        List<Mixtape> expected = List.of();

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.findAllByCreatedBy(user)).thenReturn(expected);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService);
        List<Mixtape> actual = sut.findAllForAuthenticatedUser();

        // then
        assertEquals(expected, actual);
        verify(mixtapeRepository).findAllByCreatedBy(user);
    }

    @Test
    void findAllForAuthenticatedUser_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService);
        assertThrows(UnauthorizedException.class, sut::findAllForAuthenticatedUser);

        // then
        verify(mixtapeRepository, never()).findAllByCreatedBy(any());
    }

    @Test
    void deleteById_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        // when + then
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService);
        assertThrows(UnauthorizedException.class, () -> sut.deleteById("123"));
    }

    @Test
    void deleteById_whenMixtapeDoesNotExistOrDoesNotBelongToUser_thenThrowMixtapeNotFoundException() {
        // given
        String id = "123";
        User user = new User();

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.findByIdAndCreatedBy(id, user)).thenReturn(Optional.empty());

        // when + then
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService);
        assertThrows(MixtapeNotFoundException.class, () -> sut.deleteById("123"));
    }

    @Test
    void deleteById_whenMixtapeExistsAndBelongsToUser_thenDeleteMixtape() {
        // given
        User user = new User();

        Mixtape mixtape = new Mixtape();
        mixtape.setId("123");

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.findByIdAndCreatedBy(mixtape.getId(), user)).thenReturn(Optional.of(mixtape));

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService);
        sut.deleteById(mixtape.getId());

        // then
        verify(mixtapeRepository).deleteById(mixtape.getId());
    }
}