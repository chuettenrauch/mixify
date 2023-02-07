package com.github.chuettenrauch.mixifyapi.unit.mixtape.controller;

import com.github.chuettenrauch.mixifyapi.mixtape.controller.MixtapeController;
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

@WebMvcTest(MixtapeController.class)
@ImportAutoConfiguration(classes = SecurityConfig.class)
public class MixtapeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MixtapeService mixtapeService;

    @Test
    void create_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(post("/api/mixtapes"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMixtapeJson")
    void create_whenInvalidJson_thenReturnBadRequest(String givenJson, String expectedJson) throws Exception {
        this.mvc.perform(post("/api/mixtapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login())
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void getAll_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(get("/api/mixtapes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(delete("/api/mixtapes/123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(put("/api/mixtapes/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMixtapeJson")
    void update_whenInvalidJson_thenReturnBadRequest(String givenJson, String expectedJson) throws Exception {
        this.mvc.perform(put("/api/mixtapes/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenJson)
                        .with(oauth2Login())
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void get_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(get("/api/mixtapes/123"))
                .andExpect(status().isUnauthorized());
    }

    private static Stream<Arguments> provideInvalidMixtapeJson() {
        return Stream.of(
                arguments(named("title | missing", """
                                    {
                                        "description": "some description",
                                        "imageUrl": "http://path/to/image"
                                    }
                                """),
                        """
                                {
                                    "title": "must not be blank"
                                }
                                """
                ),
                arguments(named("imageUrl | missing", """
                                    {
                                        "description": "some description",
                                        "title": "some title"
                                    }
                                """),
                        """
                                {
                                    "imageUrl": "must be an absolute URL or URL to uploaded file"
                                }
                                """
                )
        );
    }
}
