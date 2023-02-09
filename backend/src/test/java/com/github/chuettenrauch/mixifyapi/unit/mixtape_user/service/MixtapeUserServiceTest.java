package com.github.chuettenrauch.mixifyapi.unit.mixtape_user.service;

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

}