package com.github.chuettenrauch.mixifyapi.mixtape.service;

import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.exception.UnprocessableEntityException;
import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository;
import com.github.chuettenrauch.mixifyapi.mixtape_user.model.MixtapeUser;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MixtapeService {

    private final MixtapeRepository mixtapeRepository;

    private final UserService userService;

    private final MixtapeUserService mixtapeUserService;

    private final Validator validator;

    public Mixtape save(Mixtape mixtape) {
        if (mixtape.getId() != null) {
            throw new UnprocessableEntityException();
        }

        return this.mixtapeRepository.save(mixtape);
    }

    public List<Mixtape> findAllForAuthenticatedUser() {
        User user = this.userService.getAuthenticatedUser().orElseThrow(UnauthorizedException::new);

        return this.mixtapeUserService
                .findAllByUser(user)
                .stream()
                .map(MixtapeUser::getMixtape)
                .filter(Objects::nonNull)
                .toList();
    }

    public Mixtape updateByIdForAuthenticatedUser(String id, Mixtape mixtape) {
        User user = this.userService.getAuthenticatedUser()
                .orElseThrow(UnauthorizedException::new);

        if (!this.existsByIdAndCreatedByAndDraftTrue(id, user)) {
            throw new NotFoundException();
        }

        mixtape.setId(id);

        this.validateTracks(mixtape);

        return this.mixtapeRepository.save(mixtape);
    }

    public void deleteByIdForAuthenticatedUser(String id) {
        User user = this.userService.getAuthenticatedUser()
                .orElseThrow(UnauthorizedException::new);

        Mixtape mixtape = new Mixtape();
        mixtape.setId(id);

        if (!this.mixtapeUserService.existsByUserAndMixtape(user, mixtape)) {
            throw new NotFoundException();
        }

        this.mixtapeUserService.deleteByUserAndMixtape(user, mixtape);

        if (!this.mixtapeUserService.existsByMixtape(mixtape)) {
            this.mixtapeRepository.deleteById(id);
        }
    }

    public Mixtape findByIdForAuthenticatedUser(String id) {
        User user = this.userService.getAuthenticatedUser()
                .orElseThrow(UnauthorizedException::new);

        Mixtape mixtape = new Mixtape();
        mixtape.setId(id);

        return this.mixtapeUserService
                .findByUserAndMixtape(user, mixtape)
                .getMixtape();
    }

    public Mixtape findById(String id) {
        return this.mixtapeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public boolean existsById(String id) {
        return this.mixtapeRepository.existsById(id);
    }

    public boolean existsByIdAndCreatedByAndDraftTrue(String id, User createdBy) {
        return this.mixtapeRepository.existsByIdAndCreatedByAndDraftTrue(id, createdBy);
    }

    private void validateTracks(Mixtape mixtape) {
        Set<ConstraintViolation<Mixtape>> errors = validator.validateProperty(mixtape, "tracks");
        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
    }
}
