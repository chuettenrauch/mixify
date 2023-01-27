package com.github.chuettenrauch.mixifyapi.mixtape.service;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MixtapeService {

    private final com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository mixtapeRepository;

    public Mixtape save(Mixtape mixtape) {
        return this.mixtapeRepository.save(mixtape);
    }

    public List<Mixtape> findAllByCreatedBy(User user) {
        return this.mixtapeRepository.findAllByCreatedBy(user);
    }
}
