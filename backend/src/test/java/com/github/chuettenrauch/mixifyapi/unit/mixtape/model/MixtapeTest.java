package com.github.chuettenrauch.mixifyapi.unit.mixtape.model;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Track;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MixtapeTest {

    @Test
    void hasTrackWithId_whenMixtapeHasTrack_thenReturnTrue() {
        // given
        Track track = new Track();
        track.setId("123");

        Track otherTrack = new Track();
        otherTrack.setId("234");

        // when
        Mixtape sut = new Mixtape();
        sut.setTracks(List.of(otherTrack, track));

        boolean actual = sut.hasTrackWithId("123");

        // then
        assertTrue(actual);
    }

    @Test
    void hasTrackWithId_whenMixtapeDoesNotHaveTrack_thenReturnFalse() {
        // given
        Track track = new Track();
        track.setId("123");

        // when
        Mixtape sut = new Mixtape();
        sut.setTracks(List.of(track));

        boolean actual = sut.hasTrackWithId("234");

        // then
        assertFalse(actual);
    }

    @Test
    void hasTrackWithId_whenMixtapeDoesNotHaveAnyTracks_thenReturnFalse() {
        // when
        Mixtape sut = new Mixtape();
        boolean actual = sut.hasTrackWithId("123");

        // then
        assertFalse(actual);
    }

    @Test
    void hasTrackWithId_whenTrackDoesNotHaveId_thenReturnFalse() {
        // given
        Track track = new Track();

        // when
        Mixtape sut = new Mixtape();
        sut.setTracks(List.of(track));

        boolean actual = sut.hasTrackWithId("234");

        // then
        assertFalse(actual);
    }

    @Test
    void removeTrackWithId_whenTrackExistsOnMixtape_thenRemoveTrackFromTracks() {
        // given
        Track track = new Track();
        track.setId("123");

        Track otherTrack = new Track();
        otherTrack.setId("234");

        List<Track> given = List.of(otherTrack, track);
        List<Track> expected = List.of(otherTrack);

        // when
        Mixtape sut = new Mixtape();
        sut.setTracks(given);

        sut.removeTrackWithId(track.getId());

        // then
        List<Track> actual = sut.getTracks();

        assertEquals(expected, actual);
    }

    @Test
    void removeTrackWithId_whenTrackDoesNotExistOnMixtape_thenKeepTracksAsBefore() {
        // given
        Track track = new Track();
        track.setId("123");

        Track otherTrack = new Track();
        otherTrack.setId("123");

        List<Track> expected = List.of(otherTrack, track);

        // when
        Mixtape sut = new Mixtape();
        sut.setTracks(expected);

        sut.removeTrackWithId("some-other-id");

        // then
        List<Track> actual = sut.getTracks();

        assertEquals(expected, actual);
    }

    @Test
    void removeTrackWithId_whenTrackHasNoId_thenKeepTracksAsBefore() {
        // given
        Track track = new Track();
        Track otherTrack = new Track();

        List<Track> expected = List.of(otherTrack, track);

        // when
        Mixtape sut = new Mixtape();
        sut.setTracks(expected);

        sut.removeTrackWithId("some-id");

        // then
        List<Track> actual = sut.getTracks();

        assertEquals(expected, actual);
    }
}