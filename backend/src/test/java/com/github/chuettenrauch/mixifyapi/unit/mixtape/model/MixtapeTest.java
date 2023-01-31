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
}