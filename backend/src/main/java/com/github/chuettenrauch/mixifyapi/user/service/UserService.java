package com.github.chuettenrauch.mixifyapi.user.service;

import com.github.chuettenrauch.mixifyapi.auth.service.AuthService;
import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.model.UserResource;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AuthService authService;

    public Optional<User> findBySpotifyId(String spotifyId) {
        return this.userRepository.findBySpotifyId(spotifyId);
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public Optional<User> getAuthenticatedUser() {
        Authentication authentication = this.authService.getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }

        return this.findBySpotifyId(authentication.getName());
    }

    public UserResource createUserResource(OAuth2AuthorizedClient authorizedClient) {
        User user = this.getAuthenticatedUser().orElseThrow(UnauthorizedException::new);

        UserResource userResource = new UserResource(
                user.getId(),
                user.getName(),
                user.getImageUrl(),
                authorizedClient.getAccessToken().getTokenValue()
        );

        OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
        if (refreshToken != null) {
            userResource.setRefreshToken(refreshToken.getTokenValue());
        }

        return userResource;
    }

    public void deleteAuthenticatedUser() {
        User user = this.getAuthenticatedUser().orElseThrow(NotFoundException::new);

        this.userRepository.deleteById(user.getId());
    }
}
