package com.github.chuettenrauch.mixifyapi.invite.controller;

import com.github.chuettenrauch.mixifyapi.invite.service.InviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invites")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;
}
