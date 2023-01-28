package com.github.chuettenrauch.mixifyapi.integration.mixtape.controller;

import com.github.chuettenrauch.mixifyapi.file.model.File;
import com.github.chuettenrauch.mixifyapi.file.service.FileService;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository;
import com.github.chuettenrauch.mixifyapi.user.model.Provider;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MixtapeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private MixtapeRepository mixtapeRepository;

    @Test
    void create_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(post("/api/mixtapes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    void create_whenLoggedIn_thenReturnMixtape() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        this.userRepository.save(user);

        OAuth2User oAuth2User = new DefaultOAuth2User(null, Map.of(
                "email", user.getEmail()
        ), "email");

        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "some image".getBytes());
        File uploadedFile = this.fileService.saveFileForUser(file, user);

        String givenJson = String.format("""
                {
                    "title": "Best mixtape ever",
                    "description": "some nice description",
                    "image": "%s"
                }
                """, uploadedFile.getId());

        String expectedJson = String.format("""
                {
                    "title": "Best mixtape ever",
                    "description": "some nice description",
                    "image": "%s",
                    "createdBy": {
                        "id": "%s",
                        "name": "%s",
                        "imageUrl": "%s"
                    },
                    "tracks": []
                }
                """, uploadedFile.getId(), user.getId(), user.getName(), user.getImageUrl());

        // when + then
        this.mvc.perform(post("/api/mixtapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.createdAt", notNullValue()));
    }

    @Test
    @DirtiesContext
    void create_whenImageDoesNotBelongToLoggedInUser_thenReturnBadRequest() throws Exception {
        // given
        User loggedInUser = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        User fileOwner = new User("234", "simon@chipmunks.de", "simon", "/path/to/image", Provider.spotify, "user-234");

        this.userRepository.saveAll(List.of(loggedInUser, fileOwner));

        OAuth2User oAuth2User = new DefaultOAuth2User(null, Map.of(
                "email", loggedInUser.getEmail()
        ), "email");

        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "some image".getBytes());
        File uploadedFile = this.fileService.saveFileForUser(file, fileOwner);

        String givenJson = String.format("""
                {
                    "title": "Best mixtape ever",
                    "description": "some nice description",
                    "image": "%s"
                }
                """, uploadedFile.getId());

        // when + then
        this.mvc.perform(post("/api/mixtapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void getAll_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(get("/api/mixtapes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    void getAll_whenLoggedIn_thenReturnEmptyListIfNoMixtapesForTheLoggedInUserExist() throws Exception {
        // given
        User loggedInUser = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        User otherUser = new User("234", "simon@chipmunks.de", "simon", "/path/to/image", Provider.spotify, "user-234");

        this.userRepository.saveAll(List.of(loggedInUser, otherUser));

        Mixtape mixtapeOfOtherUser = new Mixtape("123", "mixtape of other user", "", "", new ArrayList<>(), LocalDateTime.now(), otherUser);
        this.mixtapeRepository.save(mixtapeOfOtherUser);

        OAuth2User oAuth2User = new DefaultOAuth2User(null, Map.of(
                "email", loggedInUser.getEmail()
        ), "email");

        // when + then
        this.mvc.perform(get("/api/mixtapes")
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isOk())
                .andExpect(content().json("[]", true));
    }

    @Test
    @DirtiesContext
    void getAll_whenLoggedIn_thenReturnMixtapesForTheLoggedUser() throws Exception {
        // given
        User loggedInUser = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        User otherUser = new User("234", "simon@chipmunks.de", "simon", "/path/to/image", Provider.spotify, "user-234");

        this.userRepository.saveAll(List.of(loggedInUser, otherUser));

        Mixtape mixtapeOfLoggedInUser = new Mixtape("123", "mixtape of other user", "description", null, new ArrayList<>(), LocalDateTime.now(), loggedInUser);
        Mixtape mixtapeOfOtherUser = new Mixtape("234", "mixtape of other user", "", null, new ArrayList<>(), LocalDateTime.now(), otherUser);

        this.mixtapeRepository.saveAll(List.of(mixtapeOfLoggedInUser, mixtapeOfOtherUser));

        OAuth2User oAuth2User = new DefaultOAuth2User(null, Map.of(
                "email", loggedInUser.getEmail()
        ), "email");

        String expectedJson = String.format("""
                        [
                            {
                                "id": "%s",
                                "title": "%s",
                                "description": "%s",
                                "image": null,
                                "createdBy": {
                                    "id": "%s",
                                    "name": "%s",
                                    "imageUrl": "%s"
                                },
                                "tracks": []
                            }
                        ]
                        """,
                mixtapeOfLoggedInUser.getId(),
                mixtapeOfLoggedInUser.getTitle(),
                mixtapeOfLoggedInUser.getDescription(),
                loggedInUser.getId(),
                loggedInUser.getName(),
                loggedInUser.getImageUrl()
        );

        // when + then
        this.mvc.perform(get("/api/mixtapes")
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

}