package com.github.chuettenrauch.mixifyapi.unit.user.service;

import com.github.chuettenrauch.mixifyapi.auth.service.AuthService;
import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.model.UserResource;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void findByEmail_delegatesToUserRepository() {
        // given
        String email = "someone@somewhere.de";
        Optional<User> expected = Optional.of(new User());

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(email)).thenReturn(expected);

        AuthService authService = mock(AuthService.class);

        // when
        UserService sut = new UserService(userRepository, authService);
        Optional<User> actual = sut.findByEmail(email);

        // then
        assertEquals(expected, actual);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void save_delegatesToUserRepository() {
        // given
        User expected = new User();

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(expected)).thenReturn(expected);

        AuthService authService = mock(AuthService.class);

        // when
        UserService sut = new UserService(userRepository, authService);
        User actual = sut.save(expected);

        // then
        assertEquals(expected, actual);
        verify(userRepository).save(expected);
    }

    @Test
    void getAuthenticatedUser_whenLoggedIn_thenReturnAuthenticatedUser() {
        // given
        User user = new User();
        user.setEmail("alvin@chipmunks.de");

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getEmail());

        AuthService authService = mock(AuthService.class);
        when(authService.getAuthentication()).thenReturn(authentication);

        // when
        UserService sut = new UserService(userRepository, authService);
        Optional<User> actual = sut.getAuthenticatedUser();

        // then
        assertTrue(actual.isPresent());
        assertEquals(user, actual.get());
    }

    @Test
    void getAuthenticatedUser_whenNotLoggedIn_thenReturnEmpty() {
        // given
        User user = new User();
        user.setEmail("alvin@chipmunks.de");

        AuthService authService = mock(AuthService.class);
        when(authService.getAuthentication()).thenReturn(null);

        UserRepository userRepository = mock(UserRepository.class);

        // when
        UserService sut = new UserService(userRepository, authService);
        Optional<User> actual = sut.getAuthenticatedUser();

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void getAuthenticatedUser_whenUserNotExists_thenReturnEmpty() {
        // given
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("not-existing-user");

        AuthService authService = mock(AuthService.class);
        when(authService.getAuthentication()).thenReturn(authentication);

        // when
        UserService sut = new UserService(userRepository, authService);
        Optional<User> actual = sut.getAuthenticatedUser();

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void createUserResource_whenCalled_thenReturnUserResource() {
        // given
        User user = new User("123", "alvin", "alvin@chipmunks.de", "/path/to/image", "user-123");
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        OAuth2AuthorizedClient authorizedClient = this.mockOAuth2AuthorizedClient(accessToken, refreshToken);

        // when
        UserService sut = mock(UserService.class);
        when(sut.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(sut.createUserResource(any())).thenCallRealMethod();

        UserResource actual = sut.createUserResource(authorizedClient);

        // then
        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getName(), actual.getName());
        assertEquals(user.getImageUrl(), actual.getImageUrl());
        assertEquals(accessToken, actual.getAccessToken());
        assertEquals(refreshToken, actual.getRefreshToken());
    }

    @Test
    void createUserResource_whenNotPresent_thenSkipRefreshToken() {
        // given
        User user = new User("123", "alvin", "alvin@chipmunks.de", "/path/to/image", "user-123");

        OAuth2AuthorizedClient authorizedClient = this.mockOAuth2AuthorizedClient("does-not-matter", null);

        // when
        UserService sut = mock(UserService.class);
        when(sut.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(sut.createUserResource(any())).thenCallRealMethod();

        UserResource actual = sut.createUserResource(authorizedClient);

        // then
        assertNull(actual.getRefreshToken());
    }

    @Test
    void createUserResource_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        OAuth2AuthorizedClient authorizedClient = this.mockOAuth2AuthorizedClient(
                "does-not-matter",
                "does-not-matter"
        );

        // when
        UserService sut = mock(UserService.class);
        when(sut.getAuthenticatedUser()).thenReturn(Optional.empty());
        when(sut.createUserResource(any())).thenCallRealMethod();

        // then
        assertThrows(UnauthorizedException.class, () -> sut.createUserResource(authorizedClient));
    }

    private OAuth2AuthorizedClient mockOAuth2AuthorizedClient(String accessToken, String refreshToken) {
        OAuth2AccessToken oAuth2AccessToken = mock(OAuth2AccessToken.class);
        when(oAuth2AccessToken.getTokenValue()).thenReturn(accessToken);

        OAuth2RefreshToken oAuth2RefreshToken = mock(OAuth2RefreshToken.class);
        when(oAuth2RefreshToken.getTokenValue()).thenReturn(refreshToken);

        OAuth2AuthorizedClient authorizedClient = mock(OAuth2AuthorizedClient.class);
        when(authorizedClient.getAccessToken()).thenReturn(oAuth2AccessToken);
        when(authorizedClient.getRefreshToken()).thenReturn(oAuth2RefreshToken);

        return authorizedClient;
    }

}