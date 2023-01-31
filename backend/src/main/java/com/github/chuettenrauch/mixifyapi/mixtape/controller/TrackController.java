package com.github.chuettenrauch.mixifyapi.mixtape.controller;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Track;
import com.github.chuettenrauch.mixifyapi.mixtape.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mixtapes/{mixtapeId}")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;

    @PostMapping
    public Track create(@PathVariable String mixtapeId, @RequestBody Track track) {
        return this.trackService.saveForMixtape(mixtapeId, track);
    }
}
