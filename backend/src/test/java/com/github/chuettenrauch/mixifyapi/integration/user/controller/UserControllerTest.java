package com.github.chuettenrauch.mixifyapi.integration.user.controller;

import com.github.chuettenrauch.mixifyapi.integration.AbstractIntegrationTest;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import com.github.chuettenrauch.mixifyapi.utils.TestUserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Client;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    private TestUserHelper testUserHelper;

    @BeforeEach
    void setup() {
        this.testUserHelper = new TestUserHelper(this.userRepository);
    }

    @Test
    void me_whenLoggedIn_thenReturnsUserResource() throws Exception {
        User user = new User("123", "alvin", "http://path/to/image.jpg", "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        // given
        String expectedJson = """
                {
                    "id": "123",
                    "name": "alvin",
                    "imageUrl": "http://path/to/image.jpg",
                    "accessToken": "access-token",
                    "refreshToken": null
                }
                """;

        // when + then
        this.mvc.perform(get("/api/users/me")
                        .with(oauth2Login().oauth2User(oAuth2User))
                        .with(this.createOAuth2Client("access-token"))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void delete_whenLoggedIn_thenLogoutAndReturnOk() throws Exception {
        // given
        User user = new User("123", "alvin", "http://path/to/image.jpg", "user-123");
        OAuth2User oAuth2User = this.testUserHelper.createLoginUser(user);

        MockHttpSession session = new MockHttpSession();

        // when + then
        this.mvc.perform(delete("/api/users/me")
                        .with(oauth2Login().oauth2User(oAuth2User))
                        .session(session)
                )
                .andExpect(status().isOk());

        assertTrue(session.isInvalid());
    }

    @Test
    @WithMockUser
    void logout_whenLoggedIn_thenInvalidatesSession() throws Exception {
        MockHttpSession session = new MockHttpSession();

        this.mvc.perform(post("/api/users/logout")
                        .session(session)
                )
                .andExpect(status().isOk());

        assertTrue(session.isInvalid());
    }

    private ClientRegistration createOAuth2ClientRegistration() {
        return ClientRegistration
                .withRegistrationId("spotify")
                .clientId("doesntmatter")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("doesntmatter")
                .redirectUri("doesntmatter")
                .tokenUri("doesntmatter")
                .build();
    }

    private SecurityMockMvcRequestPostProcessors.OAuth2ClientRequestPostProcessor createOAuth2Client(String accessToken) {
        return oauth2Client()
                .clientRegistration(this.createOAuth2ClientRegistration())
                .accessToken(new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, null, null));
    }

}