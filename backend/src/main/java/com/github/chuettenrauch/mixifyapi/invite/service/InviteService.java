package com.github.chuettenrauch.mixifyapi.invite.service;

import com.github.chuettenrauch.mixifyapi.invite.repository.InviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
}
