package com.github.chuettenrauch.mixifyapi.integration.invite.controller;

import com.github.chuettenrauch.mixifyapi.invite.model.Invite;
import com.github.chuettenrauch.mixifyapi.invite.repository.InviteRepository;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class InviteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private InviteRepository inviteRepository;

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

        Mixtape mixtape = new Mixtape("123", "my mixtape", "", "http://path/to/image", new ArrayList<>(), LocalDateTime.now(), user, true);
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

        Mixtape mixtape = new Mixtape("123", "my mixtape", "", "http://path/to/image", new ArrayList<>(), LocalDateTime.now(), user, true);
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

    @Test
    void acceptInvite_whenInviteDoesNotExist_thenReturnNotFound() throws Exception {
        // given
        User user = new User("user-123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.SPOTIFY, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        // when + then
        this.mvc.perform(put("/api/invites/123")
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void acceptInvite_whenInviteExistsButIsExpired_thenReturnGone() throws Exception {
        // given
        User user = new User("user-123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.SPOTIFY, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        Mixtape mixtape = new Mixtape("mixtape-123", "my mixtape", "", "http://path/to/image", new ArrayList<>(), LocalDateTime.now(), user, true);
        this.mixtapeRepository.save(mixtape);

        LocalDateTime expiredDate = LocalDateTime.now().minusSeconds(1);
        Invite invite = new Invite("invite-123", mixtape.getId(), expiredDate);
        this.inviteRepository.save(invite);

        // when + then
        this.mvc.perform(put("/api/invites/" +  invite.getId())
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isGone());
    }

    @Test
    void acceptInvite_whenInviteExistsAndIsNotExpired_thenReturnMixtapeUser() throws Exception {
        // given
        User user = new User("user-123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.SPOTIFY, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        Mixtape mixtape = new Mixtape("mixtape-123", "my mixtape", "", "http://path/to/image", new ArrayList<>(), LocalDateTime.now(), user, true);
        this.mixtapeRepository.save(mixtape);

        LocalDateTime notExpiredDate = LocalDateTime.now().plusHours(1);
        Invite invite = new Invite("invite-123", mixtape.getId(), notExpiredDate);
        this.inviteRepository.save(invite);

        String expectedJson = """
                {
                    "user": {
                        "id": "user-123"
                    },
                    "mixtape": {
                        "id": "mixtape-123"
                    }
                }
                """;

        // when + then
        this.mvc.perform(put("/api/invites/" +  invite.getId())
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void acceptInvite_whenInviteIsStillValidButMixtapeIsDeleted_thenReturnGone() throws Exception {
        // given
        User user = new User("user-123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.SPOTIFY, "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        LocalDateTime notExpiredDate = LocalDateTime.now().plusHours(1);
        Invite invite = new Invite("invite-123", "mixtape-id", notExpiredDate);
        this.inviteRepository.save(invite);

        // when + then
        this.mvc.perform(put("/api/invites/" +  invite.getId())
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isGone());
    }

}