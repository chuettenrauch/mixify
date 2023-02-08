package com.github.chuettenrauch.mixifyapi.unit.mixtape.controller;

import com.github.chuettenrauch.mixifyapi.invite.controller.InviteController;
import com.github.chuettenrauch.mixifyapi.invite.service.InviteService;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
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

@WebMvcTest(InviteController.class)
@ImportAutoConfiguration(classes = SecurityConfig.class)
class InviteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private InviteService inviteService;

    @MockBean
    private MixtapeService mixtapeService;

    @Test
    void create_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(post("/api/invites"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInviteJson")
    void create_whenInvalidJson_thenReturnBadRequest(String givenJson, String expectedJson) throws Exception {
        this.mvc.perform(post("/api/invites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login())
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson, true));
    }

    private static Stream<Arguments> provideInvalidInviteJson() {
        return Stream.of(
                arguments(named("mixtape | missing", """
                                    {
                                    }
                                """),
                        """
                                {
                                    "mixtape": "must not be blank"
                                }
                                """
                ),
                arguments(named("mixtape | not found", """
                                    {
                                        "mixtape": "does-not-exist"
                                    }
                                """),
                        """
                                {
                                    "mixtape": "mixtape does not exist"
                                }
                                """
                )
        );
    }
}
