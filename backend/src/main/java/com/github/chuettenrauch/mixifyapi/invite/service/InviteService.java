package com.github.chuettenrauch.mixifyapi.invite.service;

import com.github.chuettenrauch.mixifyapi.config.AppProperties;
import com.github.chuettenrauch.mixifyapi.exception.UnprocessableEntityException;
import com.github.chuettenrauch.mixifyapi.invite.model.Invite;
import com.github.chuettenrauch.mixifyapi.invite.repository.InviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;

    private final AppProperties.Invite inviteProperties;

    public Invite save(Invite invite) {
        if (invite.getId() != null) {
            throw new UnprocessableEntityException();
        }

        Duration expirationTime = Duration.parse(inviteProperties.getExpirationTime());
        invite.setExpiredAt(LocalDateTime.now().plus(expirationTime));

        return this.inviteRepository.save(invite);
    }
}
