package com.github.chuettenrauch.mixifyapi.unit.mixtape.service;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MixtapeServiceTest {

    @Test
    void save_delegatesToMixtapeRepository() {
        // given
        Mixtape expected = new Mixtape();

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.save(expected)).thenReturn(expected);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository);
        Mixtape actual = sut.save(expected);

        // then
        assertEquals(expected, actual);
        verify(mixtapeRepository).save(expected);
    }
}