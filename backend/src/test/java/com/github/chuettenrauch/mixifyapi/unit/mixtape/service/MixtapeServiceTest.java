package com.github.chuettenrauch.mixifyapi.unit.mixtape.service;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MixtapeServiceTest {

    @Test
    void save_delegatesToMixtapeRepository() {
        // given
        Mixtape expected = new Mixtape();

        com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository mixtapeRepository = mock(com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository.class);
        when(mixtapeRepository.save(expected)).thenReturn(expected);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository);
        Mixtape actual = sut.save(expected);

        // then
        assertEquals(expected, actual);
        verify(mixtapeRepository).save(expected);
    }

    @Test
    void findAllByCreatedBy_delegatesToMixtapeRepository() {
        // given
        User user = new User();
        List<Mixtape> expected = List.of();

        com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository mixtapeRepository = mock(com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository.class);
        when(mixtapeRepository.findAllByCreatedBy(user)).thenReturn(expected);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository);
        List<Mixtape> actual = sut.findAllByCreatedBy(user);

        // then
        assertEquals(expected, actual);
        verify(mixtapeRepository).findAllByCreatedBy(user);
    }
}