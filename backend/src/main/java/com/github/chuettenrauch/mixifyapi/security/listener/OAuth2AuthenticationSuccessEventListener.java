package com.github.chuettenrauch.mixifyapi.security.listener;

import com.github.chuettenrauch.mixifyapi.security.mapper.OAuth2UserMapper;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessEventListener {

    private final UserService userService;

    private final OAuth2UserMapper oAuth2UserMapper;

    @EventListener
    public void saveUserOnAuthenticationSuccess(AuthenticationSuccessEvent successEvent) {
        OAuth2LoginAuthenticationToken authentication = (OAuth2LoginAuthenticationToken) successEvent.getAuthentication();
        OAuth2User oAuth2User = authentication.getPrincipal();

        User user = this.oAuth2UserMapper.mapOAuth2UserToUser(oAuth2User, new User());

        this.userService
                .findBySpotifyId(user.getSpotifyId())
                .ifPresent(existingUser -> user.setId(existingUser.getId()));

        this.userService.save(user);
    }
}
