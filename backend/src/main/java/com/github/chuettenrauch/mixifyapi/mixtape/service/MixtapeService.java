package com.github.chuettenrauch.mixifyapi.mixtape.service;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MixtapeService {

    private final MixtapeRepository mixtapeRepository;

    public Mixtape save(Mixtape mixtape) {
        return this.mixtapeRepository.save(mixtape);
    }
}
