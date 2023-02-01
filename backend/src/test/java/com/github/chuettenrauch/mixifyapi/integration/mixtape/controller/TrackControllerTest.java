package com.github.chuettenrauch.mixifyapi.integration.mixtape.controller;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Track;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.TrackRepository;
import com.github.chuettenrauch.mixifyapi.user.model.Provider;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import com.github.chuettenrauch.mixifyapi.utils.TestUserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TrackControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MixtapeRepository mixtapeRepository;

    @Autowired
    private TrackRepository trackRepository;

    private TestUserHelper testUserHelper;

    @BeforeEach
    void setup() {
        this.testUserHelper = new TestUserHelper(this.userRepository);
    }

    @Test
    void create_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(post("/api/mixtapes/123/tracks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    void create_whenMixtapeDoesNotExist_thenReturnNotFound() throws Exception {
        // given
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser();

        String givenJson = """
                {
                    "name": "The Chipmunks Song",
                    "artist": "Alvin & The Chipmunks",
                    "imageUrl": "/path/to/image",
                    "description": null,
                    "providerUri": "spotify:track:12345"
                }
                """;

        // when + then
        this.mvc.perform(post("/api/mixtapes/123/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void create_whenMixtapeDoesNotBelongToLoggedInUser_thenReturnNotFound() throws Exception {
        // given
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser();
        User otherUser = this.testUserHelper.createUser("234");

        Mixtape mixtapeOfOtherUser = new Mixtape("123", "mixtape of other user", "", null, new ArrayList<>(), LocalDateTime.now(), otherUser);
        this.mixtapeRepository.save(mixtapeOfOtherUser);

        String givenJson = """
                {
                    "name": "The Chipmunks Song",
                    "artist": "Alvin & The Chipmunks",
                    "imageUrl": "/path/to/image",
                    "description": null,
                    "providerUri": "spotify:track:12345"
                }
                """;

        // when + then
        this.mvc.perform(post("/api/mixtapes/123/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void create_whenMixtapeExistsAndBelongsToLoggedInUser_thenReturnOk() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        Mixtape mixtape = new Mixtape("123", "mixtape", "", null, new ArrayList<>(), LocalDateTime.now(), user);
        this.mixtapeRepository.save(mixtape);

        String givenJson = """
                {
                    "name": "The Chipmunks Song",
                    "artist": "Alvin & The Chipmunks",
                    "imageUrl": "/path/to/image",
                    "description": null,
                    "providerUri": "spotify:track:12345"
                }
                """;

        // when + then
        this.mvc.perform(post("/api/mixtapes/123/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(givenJson))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DirtiesContext
    void create_whenNumOfTracksExceedsMaxLimit_thenReturnBadRequest() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Track track = new Track();
            track.setId(String.valueOf(i));

            this.trackRepository.save(track);
            tracks.add(track);
        }

        Mixtape mixtape = new Mixtape("123", "mixtape", "", null, tracks, LocalDateTime.now(), user);
        this.mixtapeRepository.save(mixtape);

        String givenJson = """
                {
                    "name": "The Chipmunks Song",
                    "artist": "Alvin & The Chipmunks",
                    "imageUrl": "/path/to/image",
                    "description": null,
                    "providerUri": "spotify:track:12345"
                }
                """;

        // when + then
        this.mvc.perform(post("/api/mixtapes/123/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(put("/api/mixtapes/123/tracks/123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_whenMixtapeNotFound_thenReturnNotFound() throws Exception {
        // given
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser();

        String givenJson = """
                {
                    "id": "234",
                    "name": "The Chipmunks Song",
                    "artist": "Alvin & The Chipmunks",
                    "imageUrl": "/path/to/image",
                    "description": null,
                    "providerUri": "spotify:track:12345"
                }
                """;

        // when + then
        this.mvc.perform(put("/api/mixtapes/123/tracks/234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void update_whenMixtapeDoesNotBelongToLoggedInUser_thenReturnNotFound() throws Exception {
        // given
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser();
        User otherUser = this.testUserHelper.createUser("234");

        Track track = new Track("234", "The Chipmunks Song", "Alvin & The Chipmunks", "/path/to/image", null, "spotify:track:12345");
        this.trackRepository.save(track);

        Mixtape mixtapeOfOtherUser = new Mixtape("123", "mixtape of other user", "", null, new ArrayList<>(List.of(track)), LocalDateTime.now(), otherUser);
        this.mixtapeRepository.save(mixtapeOfOtherUser);

        String givenJson = """
                {
                    "id": "234",
                    "name": "The Chipmunks Song",
                    "artist": "Alvin & The Chipmunks",
                    "imageUrl": "/path/to/image",
                    "description": null,
                    "providerUri": "spotify:track:12345"
                }
                """;

        // when + then
        this.mvc.perform(put("/api/mixtapes/123/tracks/234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void update_whenTrackDoesNotExistOnMixtape_thenReturnNotFound() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        Track track = new Track("234", "The Chipmunks Song", "Alvin & The Chipmunks", "/path/to/image", null, "spotify:track:12345");
        this.trackRepository.save(track);

        Mixtape mixtape = new Mixtape("123", "mixtape of other user", "", null, new ArrayList<>(), LocalDateTime.now(), user);
        this.mixtapeRepository.save(mixtape);

        String givenJson = """
                {
                    "id": "234",
                    "name": "The Chipmunks Song",
                    "artist": "Alvin & The Chipmunks",
                    "imageUrl": "/path/to/image",
                    "description": null,
                    "providerUri": "spotify:track:12345"
                }
                """;

        // when + then
        this.mvc.perform(put("/api/mixtapes/123/tracks/234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void update_whenTrackExists_thenReturnOk() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        Track track = new Track("234", "The Chipmunks Song", "Alvin & The Chipmunks", "/path/to/image", null, "spotify:track:12345");
        this.trackRepository.save(track);

        Mixtape mixtape = new Mixtape("123", "mixtape of other user", "", null, new ArrayList<>(List.of(track)), LocalDateTime.now(), user);
        this.mixtapeRepository.save(mixtape);this.mixtapeRepository.save(mixtape);

        String givenJson = """
                {
                    "id": "234",
                    "name": "The Chipmunks Song",
                    "artist": "Alvin & The Chipmunks",
                    "imageUrl": "/path/to/image",
                    "description": "Updated description",
                    "providerUri": "spotify:track:12345"
                }
                """;

        // when + then
        this.mvc.perform(put("/api/mixtapes/123/tracks/234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(givenJson));
    }

    @Test
    void delete_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(delete("/api/mixtapes/123/tracks/123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete_whenMixtapeNotFound_thenReturnNotFound() throws Exception {
        // given
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser();
        // when + then
        this.mvc.perform(delete("/api/mixtapes/123/tracks/234")
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenMixtapeDoesNotBelongToLoggedInUser_thenReturnNotFound() throws Exception {
        // given
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser();
        User otherUser = this.testUserHelper.createUser("234");

        Track track = new Track("234", "The Chipmunks Song", "Alvin & The Chipmunks", "/path/to/image", null, "spotify:track:12345");
        this.trackRepository.save(track);

        Mixtape mixtapeOfOtherUser = new Mixtape("123", "mixtape of other user", "", null, new ArrayList<>(List.of(track)), LocalDateTime.now(), otherUser);
        this.mixtapeRepository.save(mixtapeOfOtherUser);

        // when + then
        this.mvc.perform(delete("/api/mixtapes/123/tracks/234")
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenTrackDoesNotExistOnMixtape_thenReturnNotFound() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        Track track = new Track("234", "The Chipmunks Song", "Alvin & The Chipmunks", "/path/to/image", null, "spotify:track:12345");
        this.trackRepository.save(track);

        Mixtape mixtape = new Mixtape("123", "mixtape of other user", "", null, new ArrayList<>(), LocalDateTime.now(), user);
        this.mixtapeRepository.save(mixtape);

        // when + then
        this.mvc.perform(delete("/api/mixtapes/123/tracks/234")
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenTrackExists_thenReturnOk() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        Track track = new Track("234", "The Chipmunks Song", "Alvin & The Chipmunks", "/path/to/image", null, "spotify:track:12345");
        this.trackRepository.save(track);

        Mixtape mixtape = new Mixtape("123", "mixtape of other user", "", null, new ArrayList<>(List.of(track)), LocalDateTime.now(), user);
        this.mixtapeRepository.save(mixtape);this.mixtapeRepository.save(mixtape);

        // when + then
        this.mvc.perform(delete("/api/mixtapes/123/tracks/234")
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isOk());
    }
}
