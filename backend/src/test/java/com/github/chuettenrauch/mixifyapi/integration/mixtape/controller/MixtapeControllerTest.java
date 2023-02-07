package com.github.chuettenrauch.mixifyapi.integration.mixtape.controller;

import com.github.chuettenrauch.mixifyapi.file.model.File;
import com.github.chuettenrauch.mixifyapi.file.service.FileService;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository;
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
import org.springframework.mock.web.MockMultipartFile;
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
class MixtapeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private MixtapeRepository mixtapeRepository;

    private TestUserHelper testUserHelper;

    @BeforeEach
    void setup() {
        this.testUserHelper = new TestUserHelper(this.userRepository);
    }

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
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "some image".getBytes());
        File uploadedFile = this.fileService.saveFileForUser(file, user);

        String givenJson = String.format("""
                {
                    "title": "Best mixtape ever",
                    "description": "some nice description",
                    "imageUrl": "/api/files/%s"
                }
                """, uploadedFile.getId());

        String expectedJson = String.format("""
                {
                    "title": "Best mixtape ever",
                    "description": "some nice description",
                    "imageUrl": "/api/files/%s",
                    "createdBy": {
                        "id": "123",
                        "name": "alvin",
                        "imageUrl": "/path/to/image"
                    },
                    "tracks": []
                }
                """, uploadedFile.getId());

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
    void create_whenGivenJsonHasId_thenReturnUnprocessableEntity() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "some image".getBytes());
        File uploadedFile = this.fileService.saveFileForUser(file, user);

        String givenJson = String.format("""
                {
                    "id": "123",
                    "title": "Best mixtape ever",
                    "description": "some nice description",
                    "imageUrl": "/api/files/%s"
                }
                """, uploadedFile.getId());

        // when + then
        this.mvc.perform(post("/api/mixtapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isUnprocessableEntity());
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
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser();
        User otherUser = this.testUserHelper.createUser("234");

        Mixtape mixtapeOfOtherUser = new Mixtape("123", "mixtape of other user", "", "", new ArrayList<>(), LocalDateTime.now(), otherUser);
        this.mixtapeRepository.save(mixtapeOfOtherUser);

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
        String expectedJson = """
                        [
                            {
                                "id": "123",
                                "title": "mixtape of logged in user",
                                "description": "description",
                                "imageUrl": "/path/to/mixtape/image",
                                "createdBy": {
                                    "id": "123",
                                    "name": "alvin",
                                    "imageUrl": "/path/to/image"
                                },
                                "tracks": []
                            }
                        ]
                        """;

        User loggedInUser = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(loggedInUser);

        User otherUser = this.testUserHelper.createUser("234");

        Mixtape mixtapeOfLoggedInUser = new Mixtape("123", "mixtape of logged in user", "description", "/path/to/mixtape/image", new ArrayList<>(), LocalDateTime.now(), loggedInUser);
        Mixtape mixtapeOfOtherUser = new Mixtape("234", "mixtape of other user", "", null, new ArrayList<>(), LocalDateTime.now(), otherUser);

        this.mixtapeRepository.saveAll(List.of(mixtapeOfLoggedInUser, mixtapeOfOtherUser));

        // when + then
        this.mvc.perform(get("/api/mixtapes")
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void delete_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(delete("/api/mixtapes/123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete_whenLoggedInButMixtapeDoesNotExist_thenReturnNotFound() throws Exception {
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser();

        this.mvc.perform(delete("/api/mixtapes/123")
                    .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void delete_whenLoggedInButMixtapeDoesNotBelongToLoggedInUser_thenReturnNotFound() throws Exception {
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser();
        User otherUser = this.testUserHelper.createUser("234");

        Mixtape mixtapeOfOtherUser = new Mixtape("234", "mixtape of other user", "", null, new ArrayList<>(), LocalDateTime.now(), otherUser);
        this.mixtapeRepository.save(mixtapeOfOtherUser);

        this.mvc.perform(delete("/api/mixtapes/" +  mixtapeOfOtherUser.getId())
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void delete_whenLoggedInAndMixtapeBelongsToLoggedInUser_thenReturnOk() throws Exception {
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        Mixtape mixtape = new Mixtape("234", "mixtape of other user", "", null, new ArrayList<>(), LocalDateTime.now(), user);
        this.mixtapeRepository.save(mixtape);

        this.mvc.perform(delete("/api/mixtapes/" +  mixtape.getId())
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isOk());
    }

    @Test
    void update_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(put("/api/mixtapes/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    void update_whenMixtapeDoesNotExist_thenReturnNotFound() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        String givenJson = """
                {
                    "id": "123",
                    "title": "existing mixtape",
                    "description": "description",
                    "imageUrl": "http://path/to/image",
                    "createdBy": {
                        "id": "123",
                        "name": "alvin",
                        "imageUrl": "/path/to/image"
                    },
                    "tracks": []
                }
                """;

        // when + then
        this.mvc.perform(put("/api/mixtapes/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void update_whenMixtapeDoesNotBelongToLoggedInUser_thenReturnNotFound() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        User otherUser = this.testUserHelper.createUser("234");

        Mixtape mixtape = new Mixtape("234", "existing mixtape", "", null, new ArrayList<>(), LocalDateTime.now(), otherUser);
        this.mixtapeRepository.save(mixtape);

        String givenJson = """
                {
                    "id": "234",
                    "title": "existing mixtape",
                    "description": "description",
                    "imageUrl": "http://path/to/image",
                    "createdBy": {
                        "id": "123",
                        "name": "alvin",
                        "imageUrl": "/path/to/image"
                    },
                    "tracks": []
                }
                """;

        // when + then
        this.mvc.perform(put("/api/mixtapes/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void update_whenLoggedInAndMixtapeExists_thenReturnOk() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        Mixtape mixtape = new Mixtape("234", "existing mixtape", "existing description", "/path/to/mixtape/image", new ArrayList<>(), LocalDateTime.now(), user);
        this.mixtapeRepository.save(mixtape);

        String expectedJson = """
                {
                    "id": "234",
                    "title": "existing mixtape",
                    "description": "updated description",
                    "imageUrl": "http://path/to/mixtape/image",
                    "createdBy": {
                        "id": "123",
                        "name": "alvin",
                        "imageUrl": "/path/to/image"
                    },
                    "tracks": []
                }
                """;

        // when + then
        this.mvc.perform(put("/api/mixtapes/" + mixtape.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void get_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(get("/api/mixtapes/123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    void get_whenLoggedInButMixtapeDoesNotExist_thenReturnNotFound() throws Exception {
        // given
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser();

        // when + then
        this.mvc.perform(get("/api/mixtapes/123")
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void get_whenLoggedInButMixtapeDoesNotBelongToLoggedInUser_thenReturnNotFound() throws Exception {
        // given
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser();
        User otherUser = this.testUserHelper.createUser("234");

        Mixtape mixtape = new Mixtape("234", "existing mixtape", "existing description", null, new ArrayList<>(), LocalDateTime.now(), otherUser);
        this.mixtapeRepository.save(mixtape);

        // when + then
        this.mvc.perform(get("/api/mixtapes/" + mixtape.getId())
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void get_whenLoggedInAndMixtapeBelongsToLoggedInUser_thenReturnOk() throws Exception {
        // given
        String expectedJson = """
                {
                    "id": "234",
                    "title": "existing mixtape",
                    "description": "existing description",
                    "imageUrl": "/path/to/mixtape/image",
                    "createdBy": {
                        "id": "123",
                        "name": "alvin",
                        "imageUrl": "/path/to/image"
                    },
                    "tracks": []
                }
                """;

        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.spotify, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        Mixtape mixtape = new Mixtape("234", "existing mixtape", "existing description", "/path/to/mixtape/image", new ArrayList<>(), LocalDateTime.now(), user);
        this.mixtapeRepository.save(mixtape);

        // when + then
        this.mvc.perform(get("/api/mixtapes/" + mixtape.getId())
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

}