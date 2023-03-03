package com.github.chuettenrauch.mixifyapi.user.controller;

import com.github.chuettenrauch.mixifyapi.user.model.UserResource;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResource me(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        return this.userService.createUserResource(authorizedClient);
    }

    @DeleteMapping("/me")
    public void delete(HttpSession httpSession) {
        this.userService.deleteAuthenticatedUser();
        this.logout(httpSession);
    }

    @PostMapping("/logout")
    public void logout(HttpSession httpSession) {
        httpSession.invalidate();
    }
}
