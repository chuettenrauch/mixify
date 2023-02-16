package com.github.chuettenrauch.mixifyapi.unit.mixtape_user.service;

import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape_user.model.MixtapeUser;
import com.github.chuettenrauch.mixifyapi.mixtape_user.repository.MixtapeUserRepository;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MixtapeUserServiceTest {

    @Test
    void findAllForUser_whenCalled_thenDelegateToMixtapeUserRepository() {
        // given
        User user = new User();

        List<MixtapeUser> expected = List.of(
                new MixtapeUser(),
                new MixtapeUser()
        );

        MixtapeUserRepository mixtapeUserRepository = mock(MixtapeUserRepository.class);
        when(mixtapeUserRepository.findAllByUser(user)).thenReturn(expected);

        // when
        MixtapeUserService sut = new MixtapeUserService(mixtapeUserRepository);
        List<MixtapeUser> actual = sut.findAllByUser(user);

        // then
        assertEquals(expected, actual);
        verify(mixtapeUserRepository).findAllByUser(user);
    }

    @Test
    void findByUserAndMixtape_whenFound_thenReturn() {
        // given
        User user = new User();
        Mixtape mixtape = new Mixtape();

        MixtapeUser expected = new MixtapeUser(null, user, mixtape);

        MixtapeUserRepository mixtapeUserRepository = mock(MixtapeUserRepository.class);
        when(mixtapeUserRepository.findByUserAndMixtape(user, mixtape)).thenReturn(Optional.of(expected));

        // when
        MixtapeUserService sut = new MixtapeUserService(mixtapeUserRepository);
        MixtapeUser actual = sut.findByUserAndMixtape(user, mixtape);

        // then
        assertEquals(expected, actual);
        verify(mixtapeUserRepository).findByUserAndMixtape(user, mixtape);
    }

    @Test
    void findByUserAndMixtape_whenNotFound_thenThrowNotFoundException() {
        // given
        User user = new User();
        Mixtape mixtape = new Mixtape();

        MixtapeUserRepository mixtapeUserRepository = mock(MixtapeUserRepository.class);
        when(mixtapeUserRepository.findByUserAndMixtape(user, mixtape)).thenReturn(Optional.empty());

        // when
        MixtapeUserService sut = new MixtapeUserService(mixtapeUserRepository);

        assertThrows(NotFoundException.class, () -> sut.findByUserAndMixtape(user, mixtape));
    }

    @Test
    void createIfNotExists_whenMixtapeUserNotExists_thenCreateAndReturn() {
        Mixtape mixtape = new Mixtape();
        User user = new User();

        MixtapeUser expected = new MixtapeUser(null, user, mixtape);

        MixtapeUserRepository mixtapeUserRepository = mock(MixtapeUserRepository.class);
        when(mixtapeUserRepository.findByUserAndMixtape(user, mixtape)).thenReturn(Optional.empty());
        when(mixtapeUserRepository.save(expected)).thenReturn(expected);

        // when
        MixtapeUserService sut = new MixtapeUserService(mixtapeUserRepository);
        MixtapeUser actual = sut.createIfNotExists(user, mixtape);

        // then
        assertEquals(expected, actual);
        verify(mixtapeUserRepository).save(expected);
    }

    @Test
    void createIfNotExists_whenMixtapeUserAlreadyExists_thenReturn() {
        Mixtape mixtape = new Mixtape();
        User user = new User();

        MixtapeUser expected = new MixtapeUser(null, user, mixtape);

        MixtapeUserRepository mixtapeUserRepository = mock(MixtapeUserRepository.class);
        when(mixtapeUserRepository.findByUserAndMixtape(user, mixtape)).thenReturn(Optional.of(expected));
        when(mixtapeUserRepository.save(expected)).thenReturn(expected);

        // when
        MixtapeUserService sut = new MixtapeUserService(mixtapeUserRepository);
        MixtapeUser actual = sut.createIfNotExists(user, mixtape);

        // then
        assertEquals(expected, actual);
        verify(mixtapeUserRepository).save(expected);
    }

    @Test
    void existsByUserAndMixtape_whenCalled_thenDelegateToMixtapeUserRepository() {
        // given
        User user = new User();
        Mixtape mixtape = new Mixtape();
        boolean expected = true;

        MixtapeUserRepository mixtapeUserRepository = mock(MixtapeUserRepository.class);
        when(mixtapeUserRepository.existsByUserAndMixtape(user, mixtape)).thenReturn(expected);

        // when
        MixtapeUserService sut = new MixtapeUserService(mixtapeUserRepository);
        boolean actual = sut.existsByUserAndMixtape(user, mixtape);

        // then
        assertEquals(expected, actual);
        verify(mixtapeUserRepository).existsByUserAndMixtape(user, mixtape);
    }

    @Test
    void existsByMixtape_whenCalled_thenDelegateToMixtapeUserRepository() {
        // given
        Mixtape mixtape = new Mixtape();
        boolean expected = true;

        MixtapeUserRepository mixtapeUserRepository = mock(MixtapeUserRepository.class);
        when(mixtapeUserRepository.existsByMixtape(mixtape)).thenReturn(expected);

        // when
        MixtapeUserService sut = new MixtapeUserService(mixtapeUserRepository);
        boolean actual = sut.existsByMixtape(mixtape);

        // then
        assertEquals(expected, actual);
        verify(mixtapeUserRepository).existsByMixtape(mixtape);
    }

    @Test
    void deleteByUserAndMixtape_whenCalled_thenDelegateToMixtapeUserRepository() {
        // given
        User user = new User();
        Mixtape mixtape = new Mixtape();

        MixtapeUserRepository mixtapeUserRepository = mock(MixtapeUserRepository.class);

        // when
        MixtapeUserService sut = new MixtapeUserService(mixtapeUserRepository);
        sut.deleteByUserAndMixtape(user, mixtape);

        // then
        verify(mixtapeUserRepository).deleteByUserAndMixtape(user, mixtape);
    }

}