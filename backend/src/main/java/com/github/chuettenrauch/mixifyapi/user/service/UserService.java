package com.github.chuettenrauch.mixifyapi.user.service;

import com.github.chuettenrauch.mixifyapi.security.mapper.OAuth2UserMapper;
import com.github.chuettenrauch.mixifyapi.security.mapper.OAuth2UserMapperFactory;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.model.UserResource;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final OAuth2UserMapperFactory oAuth2UserMapperFactory;

    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public UserResource createUserResource(OAuth2AuthenticationToken authentication, OAuth2AuthorizedClient authorizedClient) {
        OAuth2User oAuth2User = authentication.getPrincipal();

        OAuth2UserMapper oAuth2UserMapper = this.oAuth2UserMapperFactory.getOAuth2UserMapperByProviderName(authentication.getAuthorizedClientRegistrationId());
        User user = oAuth2UserMapper.mapOAuth2UserToUser(oAuth2User, new User());

        UserResource userResource = new UserResource(
                user.getName(),
                user.getImageUrl(),
                authorizedClient.getAccessToken().getTokenValue()
        );

        OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
        if (refreshToken != null) {
            userResource.setProviderRefreshToken(refreshToken.getTokenValue());
        }

        return userResource;
    }
}
