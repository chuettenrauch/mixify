package com.github.chuettenrauch.mixifyapi.mixtape.controller;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Track;
import com.github.chuettenrauch.mixifyapi.mixtape.service.TrackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mixtapes/{mixtapeId}/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;

    @PostMapping
    @PreAuthorize("@mixtapePermissionService.canEdit(#mixtapeId)")
    public Track create(@PathVariable String mixtapeId, @Valid @RequestBody Track track) {
        return this.trackService.saveForMixtape(mixtapeId, track);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@mixtapePermissionService.canEdit(#mixtapeId)")
    public Track update(@Valid @PathVariable String mixtapeId, @PathVariable String id, @Valid @RequestBody Track track) {
        return this.trackService.updateByIdForMixtape(mixtapeId, id, track);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@mixtapePermissionService.canEdit(#mixtapeId)")
    public void delete(@PathVariable String mixtapeId, @PathVariable String id) {
        this.trackService.deleteByIdForMixtape(mixtapeId, id);
    }
}
