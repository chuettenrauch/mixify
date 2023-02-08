package com.github.chuettenrauch.mixifyapi.integration.invite.controller;

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
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InviteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MixtapeRepository mixtapeRepository;

    @Autowired
    private UserRepository userRepository;

    private TestUserHelper testUserHelper;

    @BeforeEach
    void setup() {
        this.testUserHelper = new TestUserHelper(this.userRepository);
    }

    @Test
    @DirtiesContext
    void create_whenLoggedIn_thenReturnInvite() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.SPOTIFY, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        Mixtape mixtape = new Mixtape("123", "my mixtape", "", "http://path/to/image", new ArrayList<>(), LocalDateTime.now(), user);
        this.mixtapeRepository.save(mixtape);

        String givenJson = """
                {
                    "mixtape": "123"
                }
                """;

        // when + then
        this.mvc.perform(post("/api/invites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(givenJson))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.expiredAt", notNullValue()));
    }

    @Test
    @DirtiesContext
    void create_whenGivenJsonHasId_thenReturnUnprocessableEntity() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.SPOTIFY, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        Mixtape mixtape = new Mixtape("123", "my mixtape", "", "http://path/to/image", new ArrayList<>(), LocalDateTime.now(), user);
        this.mixtapeRepository.save(mixtape);

        String givenJson = """
                {
                    "id": "123",
                    "mixtape": "123"
                }
                """;

        // when + then
        this.mvc.perform(post("/api/invites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isUnprocessableEntity());
    }

}