package com.github.chuettenrauch.mixifyapi.unit.mixtape.service;

import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Track;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.TrackRepository;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.mixtape.service.TrackService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrackServiceTest {

    @Test
    void saveForMixtape_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        String mixtapeId = "123";
        Track track = new Track();

        TrackRepository trackRepository = mock(TrackRepository.class);

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findById(mixtapeId)).thenThrow(UnauthorizedException.class);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        assertThrows(UnauthorizedException.class, () -> sut.saveForMixtape(mixtapeId, track));

        // then
        verify(trackRepository, never()).save(any());
    }

    @Test
    void saveForMixtape_whenMixtapeDoesNotExistOrDoesNotBelongToLoggedInUser_thenThrowNotFoundException() {
        // given
        String mixtapeId = "123";
        Track track = new Track();

        TrackRepository trackRepository = mock(TrackRepository.class);

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findById(mixtapeId)).thenThrow(NotFoundException.class);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        assertThrows(NotFoundException.class, () -> sut.saveForMixtape(mixtapeId, track));

        // then
        verify(trackRepository, never()).save(any());
    }

    @Test
    void saveForMixtape_whenMixtapeExist_thenSaveTrackAndLinksItToMixtape() {
        // given
        Track track = new Track();

        Mixtape mixtape = new Mixtape();
        mixtape.setId("123");

        Mixtape mixtapeWithTrackAdded = new Mixtape();
        mixtapeWithTrackAdded.setId(mixtape.getId());
        mixtapeWithTrackAdded.setTracks(List.of(track));

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findById(mixtape.getId())).thenReturn(mixtape);

        TrackRepository trackRepository = mock(TrackRepository.class);
        when(trackRepository.save(track)).thenReturn(track);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        Track actual = sut.saveForMixtape(mixtape.getId(), track);

        // then
        assertEquals(track, actual);

        verify(trackRepository).save(track);
        verify(mixtapeService).updateById(mixtapeWithTrackAdded.getId(), mixtapeWithTrackAdded);
    }
}