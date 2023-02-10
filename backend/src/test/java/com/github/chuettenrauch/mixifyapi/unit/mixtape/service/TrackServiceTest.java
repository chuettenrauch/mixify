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
        when(mixtapeService.findByIdForAuthenticatedUser(mixtapeId)).thenThrow(UnauthorizedException.class);

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
        when(mixtapeService.findByIdForAuthenticatedUser(mixtapeId)).thenThrow(NotFoundException.class);

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
        when(mixtapeService.findByIdForAuthenticatedUser(mixtape.getId())).thenReturn(mixtape);

        TrackRepository trackRepository = mock(TrackRepository.class);
        when(trackRepository.save(track)).thenReturn(track);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        Track actual = sut.saveForMixtape(mixtape.getId(), track);

        // then
        assertEquals(track, actual);

        verify(trackRepository).save(track);
        verify(mixtapeService).updateByIdForAuthenticatedUser(mixtapeWithTrackAdded.getId(), mixtapeWithTrackAdded);
    }

    @Test
    void updateByIdForMixtape_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        String mixtapeId = "123";
        String trackId = "234";

        Track track = new Track();
        track.setId(trackId);

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findByIdForAuthenticatedUser(mixtapeId)).thenThrow(UnauthorizedException.class);

        TrackRepository trackRepository = mock(TrackRepository.class);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        assertThrows(UnauthorizedException.class, () -> sut.updateByIdForMixtape(mixtapeId, trackId, track));

        // then
        verify(trackRepository, never()).save(any());
    }

    @Test
    void updateByIdForMixtape_whenMixtapeDoesNotExistOrDoesNotBelongToLoggedInUser_thenThrowNotFoundException() {
        // given
        String mixtapeId = "123";
        String trackId = "234";

        Track track = new Track();
        track.setId(trackId);

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findByIdForAuthenticatedUser(mixtapeId)).thenThrow(NotFoundException.class);

        TrackRepository trackRepository = mock(TrackRepository.class);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        assertThrows(NotFoundException.class, () -> sut.updateByIdForMixtape(mixtapeId, trackId, track));

        // then
        verify(trackRepository, never()).save(any());
    }

    @Test
    void updateByIdForMixtape_whenTrackDoesNotExist_thenThrowNotFoundException() {
        // given
        String mixtapeId = "123";
        String trackId = "234";

        Track track = new Track();
        track.setId(trackId);

        Mixtape mixtapeWithTrack = new Mixtape();
        mixtapeWithTrack.setTracks(List.of(track));

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findByIdForAuthenticatedUser(mixtapeId)).thenReturn(mixtapeWithTrack);

        TrackRepository trackRepository = mock(TrackRepository.class);
        when(trackRepository.existsById(track.getId())).thenReturn(false);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        assertThrows(NotFoundException.class, () -> sut.updateByIdForMixtape(mixtapeId, trackId, track));

        // then
        verify(trackRepository, never()).save(any());
    }

    @Test
    void updateByIdForMixtape_whenDoesNotExistOnMixtape_thenThrowNotFoundException() {
        // given
        String mixtapeId = "123";
        String trackId = "234";

        Track track = new Track();
        track.setId(trackId);

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findByIdForAuthenticatedUser(mixtapeId)).thenReturn(new Mixtape());

        TrackRepository trackRepository = mock(TrackRepository.class);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        assertThrows(NotFoundException.class, () -> sut.updateByIdForMixtape(mixtapeId, trackId, track));

        // then
        verify(trackRepository, never()).save(any());
    }

    @Test
    void updateByIdForMixtape_whenTrackExistsOnMixtape_thenEnsureIdFromUrlAndSaveTrack() {
        // given
        String trackIdFromUrl = "id-from-url";

        Track track = new Track();
        track.setId("id-from-json");

        Track expectedTrack = new Track();
        expectedTrack.setId(trackIdFromUrl);

        Mixtape mixtapeWithTrack = new Mixtape();
        mixtapeWithTrack.setId("123");
        mixtapeWithTrack.setTracks(List.of(expectedTrack));

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findByIdForAuthenticatedUser(mixtapeWithTrack.getId())).thenReturn(mixtapeWithTrack);

        TrackRepository trackRepository = mock(TrackRepository.class);
        when(trackRepository.existsById(trackIdFromUrl)).thenReturn(true);
        when(trackRepository.save(expectedTrack)).thenReturn(expectedTrack);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        Track actual = sut.updateByIdForMixtape(mixtapeWithTrack.getId(), trackIdFromUrl, track);

        // then
        assertEquals(expectedTrack, actual);
        verify(trackRepository).save(expectedTrack);
    }

    @Test
    void deleteByIdForMixtape_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        String trackId = "234";
        String mixtapeId = "123";

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findByIdForAuthenticatedUser(mixtapeId)).thenThrow(UnauthorizedException.class);

        TrackRepository trackRepository = mock(TrackRepository.class);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        assertThrows(UnauthorizedException.class, () -> sut.deleteByIdForMixtape(mixtapeId, trackId));

        // then
        verify(trackRepository, never()).deleteById(any());
    }

    @Test
    void deleteByIdForMixtape_whenMixtapeDoesNotExistOrDoesNotBelongToLoggedInUser_thenThrowNotFoundException() {
        // given
        String trackId = "234";
        String mixtapeId = "123";

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findByIdForAuthenticatedUser(mixtapeId)).thenThrow(NotFoundException.class);

        TrackRepository trackRepository = mock(TrackRepository.class);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        assertThrows(NotFoundException.class, () -> sut.deleteByIdForMixtape(mixtapeId, trackId));

        // then
        verify(trackRepository, never()).deleteById(any());
    }

    @Test
    void deleteByIdForMixtape_whenTrackDoesNotExist_thenThrowNotFoundException() {
        // given
        String trackId = "234";
        String mixtapeId = "123";

        Track track = new Track();
        track.setId(trackId);

        Mixtape mixtapeWithTrack = new Mixtape();
        mixtapeWithTrack.setId(mixtapeId);
        mixtapeWithTrack.setTracks(List.of(track));

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findByIdForAuthenticatedUser(mixtapeWithTrack.getId())).thenReturn(mixtapeWithTrack);

        TrackRepository trackRepository = mock(TrackRepository.class);
        when(trackRepository.existsById(track.getId())).thenReturn(false);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        assertThrows(NotFoundException.class, () -> sut.deleteByIdForMixtape(mixtapeId, trackId));

        // then
        verify(trackRepository, never()).deleteById(any());
    }

    @Test
    void deleteByIdForMixtape_whenTrackDoesNotExistOnMixtape_thenThrowNotFoundException() {
        // given
        String trackId = "234";
        String mixtapeId = "123";

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findByIdForAuthenticatedUser(mixtapeId)).thenReturn(new Mixtape());

        TrackRepository trackRepository = mock(TrackRepository.class);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        assertThrows(NotFoundException.class, () -> sut.deleteByIdForMixtape(mixtapeId, trackId));

        // then
        verify(trackRepository, never()).deleteById(any());
    }

    @Test
    void deleteByIdForMixtape_whenTrackExistsOnMixtape_thenDeleteAndUpdateMixtape() {
        // given
        Track track = new Track();
        track.setId("234");

        Mixtape mixtapeWithTrack = new Mixtape();
        mixtapeWithTrack.setId("123");
        mixtapeWithTrack.setTracks(List.of(track));

        MixtapeService mixtapeService = mock(MixtapeService.class);
        when(mixtapeService.findByIdForAuthenticatedUser(mixtapeWithTrack.getId())).thenReturn(mixtapeWithTrack);

        TrackRepository trackRepository = mock(TrackRepository.class);
        when(trackRepository.existsById(track.getId())).thenReturn(true);

        // when
        TrackService sut = new TrackService(trackRepository, mixtapeService);
        sut.deleteByIdForMixtape(mixtapeWithTrack.getId(), track.getId());

        // then
        verify(trackRepository).deleteById(track.getId());
        verify(mixtapeService).updateByIdForAuthenticatedUser(mixtapeWithTrack.getId(), mixtapeWithTrack);
    }
}