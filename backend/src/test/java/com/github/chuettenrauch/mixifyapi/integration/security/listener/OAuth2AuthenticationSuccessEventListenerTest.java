package com.github.chuettenrauch.mixifyapi.integration.security.listener;

import com.github.chuettenrauch.mixifyapi.security.listener.OAuth2AuthenticationSuccessEventListener;
import com.github.chuettenrauch.mixifyapi.user.model.Provider;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class OAuth2AuthenticationSuccessEventListenerTest {

    @Autowired
    private OAuth2AuthenticationSuccessEventListener sut;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DirtiesContext
    void saveUserOnAuthenticationSuccess_createsNewUserIfUserDoesNotExistAlready() {
        // given
        String expectedImageUrl = "http://url/to/image-1.jpg";

        List<Map<String, String>> images = new ArrayList<>(List.of(
                Map.of(
                        "url", expectedImageUrl
                )
        ));

        Map<String, Object> attributes = Map.of(
                "email", "alvin@chipmunks.de",
                "display_name", "Alvin Chipmunk",
                "id", "user-123",
                "images", images
        );

        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(oAuth2User.getAttributes()).thenReturn(attributes);

        AuthenticationSuccessEvent successEvent = this.createAuthenticationSuccessEvent(oAuth2User);

        // when
        sut.saveUserOnAuthenticationSuccess(successEvent);

        // then
        Optional<User> savedUserOptional = this.userRepository.findByEmail((String) attributes.get("email"));

        assertTrue(savedUserOptional.isPresent());

        User savedUser = savedUserOptional.get();

        assertEquals(attributes.get("email"), savedUser.getEmail());
        assertEquals(attributes.get("display_name"), savedUser.getName());
        assertEquals(expectedImageUrl, savedUser.getImageUrl());
        assertEquals(Provider.spotify, savedUser.getProvider());
        assertEquals(attributes.get("id"), savedUser.getProviderId());
    }

    @Test
    @DirtiesContext
    void saveUserOnAuthenticationSuccess_updatedExistingUser() {
        // given
        User existingUser = new User(
                "123",
                "alvin@chipmunks.de",
                "should be overwritten",
                "should be overwritten",
                Provider.spotify,
                "user-123"
        );

        this.userRepository.save(existingUser);

        String expectedImageUrl = "updated image url";

        Map<String, Object> attributes = Map.of(
                "email", existingUser.getEmail(),
                "display_name", "updated name",
                "id", existingUser.getId(),
                "images", new ArrayList<>(List.of(
                        Map.of(
                                "url", expectedImageUrl
                        )
                ))
        );

        OAuth2User oAuth2User = new DefaultOAuth2User(null, attributes, "display_name");

        AuthenticationSuccessEvent successEvent = this.createAuthenticationSuccessEvent(oAuth2User);

        // when
        sut.saveUserOnAuthenticationSuccess(successEvent);

        // then
        Optional<User> savedUserOptional = this.userRepository.findByEmail(existingUser.getEmail());

        assertTrue(savedUserOptional.isPresent());

        User savedUser = savedUserOptional.get();

        assertEquals(attributes.get("display_name"), savedUser.getName());
        assertEquals(expectedImageUrl, savedUser.getImageUrl());
    }

    @Test
    void saveUserOnAuthenticationSuccess_doesNotCreateUserIfNotSpotifyLogin() {
        Map<String, Object> attributes = Map.of(
                "email", "alvin@chipmunks.de",
                "display_name", "Alvin Chipmunk",
                "id", "user-123",
                "images", new ArrayList<>()
        );

        OAuth2User oAuth2User = new DefaultOAuth2User(null, attributes, "display_name");

        AuthenticationSuccessEvent successEvent = this.createAuthenticationSuccessEvent(oAuth2User, "other-provider");

        // when
        sut.saveUserOnAuthenticationSuccess(successEvent);

        // then

        Optional<User> user = this.userRepository.findByEmail((String) attributes.get("email"));

        assertTrue(user.isEmpty());
    }

    private AuthenticationSuccessEvent createAuthenticationSuccessEvent(OAuth2User oAuth2User) {
        return this.createAuthenticationSuccessEvent(oAuth2User, Provider.spotify.toString());
    }

    private AuthenticationSuccessEvent createAuthenticationSuccessEvent(OAuth2User oAuth2User, String providerName) {
        ClientRegistration clientRegistration = ClientRegistration
                .withRegistrationId(providerName)
                .clientId("doesntmatter")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("doesntmatter")
                .redirectUri("doesntmatter")
                .tokenUri("doesntmatter")
                .build();

        OAuth2LoginAuthenticationToken authentication = mock(OAuth2LoginAuthenticationToken.class);
        when(authentication.getClientRegistration()).thenReturn(clientRegistration);
        when(authentication.getPrincipal()).thenReturn(oAuth2User);

        return new AuthenticationSuccessEvent(authentication);
    }

}
