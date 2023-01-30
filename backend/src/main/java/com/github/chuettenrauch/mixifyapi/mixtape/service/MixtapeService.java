package com.github.chuettenrauch.mixifyapi.mixtape.service;

import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.mixtape.exception.MixtapeNotFoundException;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MixtapeService {

    private final MixtapeRepository mixtapeRepository;

    private final UserService userService;

    public Mixtape save(Mixtape mixtape) {
        return this.mixtapeRepository.save(mixtape);
    }

    public List<Mixtape> findAllForAuthenticatedUser() {
        User user = this.userService.getAuthenticatedUser().orElseThrow(UnauthorizedException::new);

        return this.mixtapeRepository.findAllByCreatedBy(user);
    }

    public void deleteById(String id) {
        User user = this.userService.getAuthenticatedUser()
                .orElseThrow(UnauthorizedException::new);

        Mixtape mixtape = this.mixtapeRepository.findByIdAndCreatedBy(id, user)
                .orElseThrow(MixtapeNotFoundException::new);

        this.mixtapeRepository.deleteById(mixtape.getId());
    }
}
