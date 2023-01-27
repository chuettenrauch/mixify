package com.github.chuettenrauch.mixifyapi.mixtape.controller;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mixtapes")
@RequiredArgsConstructor
public class MixtapeController {

    private final MixtapeService mixtapeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mixtape create(@RequestBody Mixtape mixtape) {
        return this.mixtapeService.save(mixtape);
    }
}
