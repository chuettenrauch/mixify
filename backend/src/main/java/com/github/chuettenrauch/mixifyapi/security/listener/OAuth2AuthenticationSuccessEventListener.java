package com.github.chuettenrauch.mixifyapi.security.listener;

import com.github.chuettenrauch.mixifyapi.security.exception.NoSuchOAuth2MapperException;
import com.github.chuettenrauch.mixifyapi.security.mapper.OAuth2UserMapper;
import com.github.chuettenrauch.mixifyapi.security.mapper.OAuth2UserMapperFactory;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessEventListener {

    private final UserService userService;

    private final OAuth2UserMapperFactory oAuth2UserMapperFactory;

    @EventListener
    public void saveUserOnAuthenticationSuccess(AuthenticationSuccessEvent successEvent) {
        OAuth2LoginAuthenticationToken authentication = (OAuth2LoginAuthenticationToken) successEvent.getAuthentication();
        OAuth2User oAuth2User = authentication.getPrincipal();
        ClientRegistration clientRegistration = authentication.getClientRegistration();

        OAuth2UserMapper oAuth2UserMapper;

        try {
            oAuth2UserMapper = this.oAuth2UserMapperFactory.getOAuth2UserMapperByProviderName(
                    clientRegistration.getRegistrationId()
            );
        } catch (NoSuchOAuth2MapperException e) {
            return;
        }

        User user = oAuth2UserMapper.mapOAuth2UserToUser(oAuth2User, new User());

        this.userService
                .findByEmail(user.getEmail())
                .ifPresent(existingUser -> user.setId(existingUser.getId()));

        this.userService.save(user);
    }
}
