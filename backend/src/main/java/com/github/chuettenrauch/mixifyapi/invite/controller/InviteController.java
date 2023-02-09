package com.github.chuettenrauch.mixifyapi.invite.controller;

import com.github.chuettenrauch.mixifyapi.invite.model.Invite;
import com.github.chuettenrauch.mixifyapi.invite.service.InviteService;
import com.github.chuettenrauch.mixifyapi.mixtape_user.model.MixtapeUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invites")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Invite create(@RequestBody @Valid Invite invite) {
        return this.inviteService.save(invite);
    }

    @PutMapping("/{id}")
    public MixtapeUser acceptInvite(@PathVariable String id) {
        return this.inviteService.acceptInviteByIdForAuthenticatedUser(id);
    }
}
