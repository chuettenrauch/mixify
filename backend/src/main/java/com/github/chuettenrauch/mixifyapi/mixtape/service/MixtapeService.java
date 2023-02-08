package com.github.chuettenrauch.mixifyapi.mixtape.service;

import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.exception.UnprocessableEntityException;
import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MixtapeService {

    private final MixtapeRepository mixtapeRepository;

    private final UserService userService;

    private final Validator validator;

    public Mixtape save(Mixtape mixtape) {
        if (mixtape.getId() != null) {
            throw new UnprocessableEntityException();
        }

        return this.mixtapeRepository.save(mixtape);
    }

    public List<Mixtape> findAllForAuthenticatedUser() {
        User user = this.userService.getAuthenticatedUser().orElseThrow(UnauthorizedException::new);

        return this.mixtapeRepository.findAllByCreatedBy(user);
    }

    public Mixtape updateById(String id, Mixtape mixtape) {
        User user = this.userService.getAuthenticatedUser()
                .orElseThrow(UnauthorizedException::new);

        if (!this.mixtapeRepository.existsByIdAndCreatedBy(id, user)) {
            throw new NotFoundException();
        }

        mixtape.setId(id);

        this.validateTracks(mixtape);

        return this.mixtapeRepository.save(mixtape);
    }

    public void deleteById(String id) {
        User user = this.userService.getAuthenticatedUser()
                .orElseThrow(UnauthorizedException::new);

        if (!this.mixtapeRepository.existsByIdAndCreatedBy(id, user)) {
            throw new NotFoundException();
        }

        this.mixtapeRepository.deleteById(id);
    }

    public Mixtape findById(String id) {
        User user = this.userService.getAuthenticatedUser()
                .orElseThrow(UnauthorizedException::new);

        return this.mixtapeRepository.findByIdAndCreatedBy(id, user)
                .orElseThrow(NotFoundException::new);
    }

    public boolean existsById(String id) {
        return this.mixtapeRepository.existsById(id);
    }

    private void validateTracks(Mixtape mixtape) {
        Set<ConstraintViolation<Mixtape>> errors = validator.validateProperty(mixtape, "tracks");
        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
    }
}
