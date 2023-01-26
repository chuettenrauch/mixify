package com.github.chuettenrauch.mixifyapi.unit.user.service;

import com.github.chuettenrauch.mixifyapi.security.mapper.OAuth2UserMapper;
import com.github.chuettenrauch.mixifyapi.security.mapper.OAuth2UserMapperFactory;
import com.github.chuettenrauch.mixifyapi.user.model.Provider;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.model.UserResource;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

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

        OAuth2UserMapperFactory oAuth2UserMapperFactory = mock(OAuth2UserMapperFactory.class);

        // when
        UserService sut = new UserService(userRepository, oAuth2UserMapperFactory);
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

        OAuth2UserMapperFactory oAuth2UserMapperFactory = mock(OAuth2UserMapperFactory.class);

        // when
        UserService sut = new UserService(userRepository, oAuth2UserMapperFactory);
        User actual = sut.save(expected);

        // then
        assertEquals(expected, actual);
        verify(userRepository).save(expected);
    }

    @Test
    void createUserResourceFromAuthentication_returnsUserResource() {
        // given
        User user = new User("123", "alvin", "alvin@chipmunks.de", "/path/to/image", Provider.spotify, "user-123");
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        OAuth2User oAuth2User = mock(OAuth2User.class);

        OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
        when(authentication.getPrincipal()).thenReturn(oAuth2User);

        OAuth2AuthorizedClient authorizedClient = this.mockOAuth2AuthorizedClient(accessToken, refreshToken);
        OAuth2UserMapperFactory oAuth2UserMapperFactory = this.mockOAuth2UserMapperFactory(user);

        UserRepository userRepository = mock(UserRepository.class);

        // when
        UserService sut = new UserService(userRepository, oAuth2UserMapperFactory);
        UserResource actual = sut.createUserResource(authentication, authorizedClient);

        // then
        assertEquals(user.getName(), actual.getName());
        assertEquals(user.getImageUrl(), actual.getImageUrl());
        assertEquals(accessToken, actual.getProviderAccessToken());
        assertEquals(refreshToken, actual.getProviderRefreshToken());
    }

    @Test
    void createUserResourceFromAuthentication_skipsRefreshTokenIfNotPresent() {
        // given
        User user = new User("123", "alvin", "alvin@chipmunks.de", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = mock(OAuth2User.class);

        OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
        when(authentication.getPrincipal()).thenReturn(oAuth2User);

        OAuth2AuthorizedClient authorizedClient = this.mockOAuth2AuthorizedClient("does-not-matter", null);
        OAuth2UserMapperFactory oAuth2UserMapperFactory = this.mockOAuth2UserMapperFactory(user);

        UserRepository userRepository = mock(UserRepository.class);

        // when
        UserService sut = new UserService(userRepository, oAuth2UserMapperFactory);
        UserResource actual = sut.createUserResource(authentication, authorizedClient);

        // then
        assertNull(actual.getProviderRefreshToken());
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

    private OAuth2UserMapperFactory mockOAuth2UserMapperFactory(User returnedUser) {
        OAuth2UserMapper oAuth2UserMapper = mock(OAuth2UserMapper.class);
        when(oAuth2UserMapper.mapOAuth2UserToUser(any(), any())).thenReturn(returnedUser);

        OAuth2UserMapperFactory oAuth2UserMapperFactory = mock(OAuth2UserMapperFactory.class);
        when(oAuth2UserMapperFactory.getOAuth2UserMapperByProviderName(any())).thenReturn(oAuth2UserMapper);

        return oAuth2UserMapperFactory;
    }

}