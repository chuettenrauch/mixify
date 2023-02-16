package com.github.chuettenrauch.mixifyapi.mixtape.security;

import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MixtapePermissionService {

    private final MixtapeService mixtapeService;

    private final MixtapeUserService mixtapeUserService;

    private final UserService userService;

    public boolean canView(String mixtapeId) {
        User user = this.userService.getAuthenticatedUser().orElseThrow(UnauthorizedException::new);

        Mixtape mixtape = new Mixtape();
        mixtape.setId(mixtapeId);

        return this.mixtapeUserService.existsByUserAndMixtape(user, mixtape);
    }

    public boolean canEdit(String mixtapeId) {
        User user = this.userService.getAuthenticatedUser().orElseThrow(UnauthorizedException::new);

        return this.mixtapeService.existsByIdAndCreatedByAndDraftTrue(mixtapeId, user);
    }
}
