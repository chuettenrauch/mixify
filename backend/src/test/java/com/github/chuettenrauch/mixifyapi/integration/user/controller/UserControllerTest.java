package com.github.chuettenrauch.mixifyapi.integration.user.controller;

import com.github.chuettenrauch.mixifyapi.user.model.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Client;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DirtiesContext
    void me_returnsUserResourceIfLoggedIn() throws Exception {
        // given
        String expectedJson = """
                {
                    "name": "alvin",
                    "imageUrl": "http://path/to/image.jpg",
                    "providerAccessToken": "access-token",
                    "providerRefreshToken": null
                }
                """;

        // when + then
        this.mvc.perform(get("/api/users/me")
                        .with(this.createOAuth2Login("alvin", "http://path/to/image.jpg"))
                        .with(this.createOAuth2Client("access-token"))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    @WithMockUser
    void logout_invalidatesSession() throws Exception {
        MockHttpSession session = new MockHttpSession();

        this.mvc.perform(post("/api/users/logout")
                        .session(session)
                )
                .andExpect(status().isOk());

        assertTrue(session.isInvalid());
    }

    private ClientRegistration createOAuth2ClientRegistration() {
        return ClientRegistration
                .withRegistrationId(Provider.SPOTIFY.toString())
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

    private SecurityMockMvcRequestPostProcessors.OAuth2LoginRequestPostProcessor createOAuth2Login(String name, String imageUrl) {
        return oauth2Login()
                .clientRegistration(this.createOAuth2ClientRegistration())
                .attributes(attrs -> {
                    attrs.put("display_name", name);
                    attrs.put("email", "alvi@chipmunks.de");
                    attrs.put("images", new ArrayList<>(List.of(
                            Map.of(
                                    "url", imageUrl
                            )
                    )));
                    attrs.put("id", "user-123");
                });
    }

}