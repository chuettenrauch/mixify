package com.github.chuettenrauch.mixifyapi.invite.service;

import com.github.chuettenrauch.mixifyapi.config.AppProperties;
import com.github.chuettenrauch.mixifyapi.exception.GoneException;
import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.exception.UnprocessableEntityException;
import com.github.chuettenrauch.mixifyapi.invite.model.Invite;
import com.github.chuettenrauch.mixifyapi.invite.repository.InviteRepository;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.mixtape_user.model.MixtapeUser;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;

    private final UserService userService;

    private final MixtapeService mixtapeService;

    private final MixtapeUserService mixtapeUserService;

    private final AppProperties appProperties;

    public Invite save(Invite invite) {
        if (invite.getId() != null) {
            throw new UnprocessableEntityException();
        }

        Duration expirationTime = Duration.parse(appProperties.getInvite().getExpirationTime());
        invite.setExpiredAt(LocalDateTime.now().plus(expirationTime));

        return this.inviteRepository.save(invite);
    }

    public MixtapeUser acceptInviteByIdForAuthenticatedUser(String id) {
        User user = this.userService.getAuthenticatedUser().orElseThrow(UnauthorizedException::new);
        Invite invite = this.inviteRepository.findById(id).orElseThrow(NotFoundException::new);

        if (invite.isExpired()) {
            throw new GoneException();
        }

        try {
            Mixtape mixtape = this.mixtapeService.findById(invite.getMixtape());

            return this.mixtapeUserService.createIfNotExists(user, mixtape);
        } catch (NotFoundException e) {
            throw new GoneException();
        }
    }
}
