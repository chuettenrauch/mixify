package com.github.chuettenrauch.mixifyapi.unit.mixtape.controller;

import com.github.chuettenrauch.mixifyapi.mixtape.controller.TrackController;
import com.github.chuettenrauch.mixifyapi.mixtape.service.TrackService;
import com.github.chuettenrauch.mixifyapi.security.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrackController.class)
@ImportAutoConfiguration(classes = SecurityConfig.class)
class TrackControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TrackService trackService;

    @Test
    void create_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(post("/api/mixtapes/123/tracks"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTrackJson")
    void create_whenInvalidJson_thenReturnBadRequest(String givenJson, String expectedJson) throws Exception {
        this.mvc.perform(post("/api/mixtapes/123/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login())
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void update_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(put("/api/mixtapes/123/tracks/123"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTrackJson")
    void update_whenInvalidJson_thenReturnBadRequest(String givenJson, String expectedJson) throws Exception {
        this.mvc.perform(put("/api/mixtapes/123/tracks/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login())
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void delete_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(delete("/api/mixtapes/123/tracks/123"))
                .andExpect(status().isUnauthorized());
    }

    private static Stream<Arguments> provideInvalidTrackJson() {
        return Stream.of(
                arguments(named("name | missing", """
                                    {
                                        "artist": "some artist",
                                        "imageUrl": "http://path/to/image",
                                        "providerUri": "spotify:track:234"
                                    }
                                """),
                        """
                                {
                                    "name": "must not be blank"
                                }
                                """
                ),
                arguments(named("artist | missing", """
                                    {
                                        "name": "some name",
                                        "imageUrl": "http://path/to/image",
                                        "providerUri": "spotify:track:234"
                                    }
                                """),
                        """
                                {
                                    "artist": "must not be blank"
                                }
                                """
                ),
                arguments(named("imageUrl | missing", """
                                    {
                                        "name": "some name",
                                        "artist": "some artist",
                                        "providerUri": "spotify:track:234"
                                    }
                                """),
                        """
                                {
                                    "imageUrl": "must be an absolute URL or URL to uploaded file"
                                }
                                """
                ),
                arguments(named("imageUrl | missing", """
                                    {
                                        "name": "some name",
                                        "artist": "some artist",
                                        "imageUrl": "http://path/to/image"
                                    }
                                """),
                        """
                                {
                                    "providerUri": "not a valid provider uri"
                                }
                                """
                )
        );
    }
}
