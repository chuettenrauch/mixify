package com.github.chuettenrauch.mixifyapi.mixtape_user.service;

import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.invite.model.Invite;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.mixtape_user.model.MixtapeUser;
import com.github.chuettenrauch.mixifyapi.mixtape_user.repository.MixtapeUserRepository;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MixtapeUserService {

    private final MixtapeUserRepository mixtapeUserRepository;

    private final MixtapeService mixtapeService;

    private final UserService userService;

    public MixtapeUser createFromInviteForAuthenticatedUserIfNotExists(Invite invite) {
        User user = this.userService.getAuthenticatedUser().orElseThrow(UnauthorizedException::new);
        Mixtape mixtape = this.mixtapeService.findById(invite.getMixtape());

        MixtapeUser mixtapeUser = this.mixtapeUserRepository
                .findOneByUserAndMixtape(user, mixtape)
                .orElse(new MixtapeUser(null, user, mixtape));

        return this.mixtapeUserRepository.save(mixtapeUser);
    }
}
