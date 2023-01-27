package com.github.chuettenrauch.mixifyapi.audit.provider;

import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorProvider implements AuditorAware<String> {

    private final UserService userService;

    @Override
    public Optional<String> getCurrentAuditor() {
        return this.userService.getAuthenticatedUser().map(User::getId);
    }
}
