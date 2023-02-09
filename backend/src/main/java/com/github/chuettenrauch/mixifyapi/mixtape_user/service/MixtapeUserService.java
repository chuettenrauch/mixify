package com.github.chuettenrauch.mixifyapi.mixtape_user.service;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape_user.model.MixtapeUser;
import com.github.chuettenrauch.mixifyapi.mixtape_user.repository.MixtapeUserRepository;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MixtapeUserService {

    private final MixtapeUserRepository mixtapeUserRepository;

    public List<MixtapeUser> findAllByUser(User user) {
        return this.mixtapeUserRepository.findAllByUser(user);
    }

    public MixtapeUser createIfNotExists(User user, Mixtape mixtape) {
        MixtapeUser mixtapeUser = this.mixtapeUserRepository
                .findByUserAndMixtape(user, mixtape)
                .orElse(new MixtapeUser(null, user, mixtape));

        return this.mixtapeUserRepository.save(mixtapeUser);
    }
}
