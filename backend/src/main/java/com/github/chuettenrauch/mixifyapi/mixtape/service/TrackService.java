package com.github.chuettenrauch.mixifyapi.mixtape.service;

import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Track;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;
    private final MixtapeService mixtapeService;

    public Track saveForMixtape(String mixtapeId, Track track) {
        Mixtape mixtape = this.mixtapeService.findById(mixtapeId);

        Track savedTrack = this.trackRepository.save(track);

        mixtape.getTracks().add(track);
        this.mixtapeService.updateById(mixtape.getId(), mixtape);

        return savedTrack;
    }

    public Track updateByIdForMixtape(String mixtapeId, String id, Track track) {
        track.setId(id);
        this.mixtapeService.findById(mixtapeId);

        if (!this.trackRepository.existsById(track.getId())) {
            throw new NotFoundException();
        }

        return this.trackRepository.save(track);
    }

}
