package com.github.chuettenrauch.mixifyapi.security.listener;

import com.github.chuettenrauch.mixifyapi.security.mapper.OAuth2UserMapper;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuth2AuthenticationSuccessEventListener {

    private final UserService userService;

    private final Map<String, OAuth2UserMapper> oAuth2UserMapperByProvider;

    public OAuth2AuthenticationSuccessEventListener(UserService userService, List<OAuth2UserMapper> oAuth2UserMappers) {
        this.userService = userService;
        this.oAuth2UserMapperByProvider = oAuth2UserMappers
                .stream()
                .collect(
                        Collectors.toMap(oAuth2UserMapper -> oAuth2UserMapper.getProvider().toString(), Function.identity())
                );
    }

    @EventListener
    public void saveUserOnAuthenticationSuccess(AuthenticationSuccessEvent successEvent) {
        OAuth2LoginAuthenticationToken authentication = (OAuth2LoginAuthenticationToken) successEvent.getAuthentication();
        OAuth2User oAuth2User = authentication.getPrincipal();
        ClientRegistration clientRegistration = authentication.getClientRegistration();

        String providerName = clientRegistration.getRegistrationId();
        if (!this.oAuth2UserMapperByProvider.containsKey(providerName)) {
            return;
        }

        OAuth2UserMapper oAuth2UserMapper = this.oAuth2UserMapperByProvider.get(providerName);

        User user = oAuth2UserMapper.mapOAuth2UserToUser(oAuth2User, new User());

        this.userService
                .findByEmail(user.getEmail())
                .ifPresent(existingUser -> user.setId(existingUser.getId()));

        this.userService.save(user);
    }
}
