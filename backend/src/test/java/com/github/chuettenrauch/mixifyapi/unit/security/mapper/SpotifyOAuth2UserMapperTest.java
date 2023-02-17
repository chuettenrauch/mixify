package com.github.chuettenrauch.mixifyapi.unit.security.mapper;

import com.github.chuettenrauch.mixifyapi.security.exception.OAuth2MappingException;
import com.github.chuettenrauch.mixifyapi.security.mapper.SpotifyOAuth2UserMapper;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpotifyOAuth2UserMapperTest {


    @Test
    void mapToUser_mapsOAuth2UserToUser() {
        // given
        String expectedName = "Alvin Chipmunk";
        String expectedImageUrl = "http://url/to/image-1.jpg";
        String expectedSpotifyId = "user-123";

        List<Map<String, String>> images = new ArrayList<>(List.of(
                Map.of(
                        "url", expectedImageUrl
                ),
                Map.of(
                        "url", "http://url/to/image-2.jpg"
                )
        ));

        OAuth2User oAuth2User = mock(OAuth2User.class);

        when(oAuth2User.getAttributes()).thenReturn(Map.of(
                "display_name", expectedName,
                "id", expectedSpotifyId,
                "images", images
        ));

        // when
        SpotifyOAuth2UserMapper sut = new SpotifyOAuth2UserMapper();
        User actual = sut.mapOAuth2UserToUser(oAuth2User, new User());

        // then
        assertEquals(expectedName, actual.getName());
        assertEquals(expectedImageUrl, actual.getImageUrl());
        assertEquals(expectedSpotifyId, actual.getSpotifyId());
    }

    @Test
    void mapToUser_whenImagesEmpty_thenDoesNotSetImageUrl() {
        // given
        OAuth2User oAuth2User = mock(OAuth2User.class);

        when(oAuth2User.getAttributes()).thenReturn(Map.of(
                "email", "alvin@chipmunks.de",
                "display_name", "Alvin Chipmunk",
                "id", "user-123",
                "images", new ArrayList<>()
        ));

        // when
        SpotifyOAuth2UserMapper sut = new SpotifyOAuth2UserMapper();
        User actual = sut.mapOAuth2UserToUser(oAuth2User, new User());

        // then
        assertNull(actual.getImageUrl());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidAttributes")
    void mapToUser_throwsOAuth2UserMapperExceptionIfDataIsRequiredDataIsNotPresent(Map<String, Object> attributes) {
        // given
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(oAuth2User.getAttributes()).thenReturn(attributes);

        User user = new User();

        // when + then
        SpotifyOAuth2UserMapper sut = new SpotifyOAuth2UserMapper();
        assertThrows(OAuth2MappingException.class, () -> sut.mapOAuth2UserToUser(oAuth2User, user));

    }

    private static Stream<Arguments> provideInvalidAttributes() {
        return Stream.of(
                arguments(named("display_name | missing", Map.of(
                        "id", "user-123"
                ))),
                arguments(named("display_name | null", createAttributes(null, "user-123"))),
                arguments(named("display_name | empty string", createAttributes("", "user-123"))),
                arguments(named("id | missing", Map.of(
                        "display_name", "alvin"
                ))),
                arguments(named("id | null", createAttributes("alvin", null))),
                arguments(named("id | empty string", createAttributes("alvin", "")))
        );
    }

    private static Map<String, String> createAttributes(String displayName, String id) {
        Map<String, String> attributes = new HashMap<>();

        attributes.put("display_name", displayName);
        attributes.put("id", id);

        return attributes;
    }
}