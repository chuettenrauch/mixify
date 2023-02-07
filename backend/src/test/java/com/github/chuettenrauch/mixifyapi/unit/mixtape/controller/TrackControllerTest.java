package com.github.chuettenrauch.mixifyapi.unit.mixtape.controller;

import com.github.chuettenrauch.mixifyapi.mixtape.controller.TrackController;
import com.github.chuettenrauch.mixifyapi.mixtape.service.TrackService;
import com.github.chuettenrauch.mixifyapi.security.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrackController.class)
@ImportAutoConfiguration(classes = SecurityConfig.class)
public class TrackControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TrackService trackService;

    @Test
    void create_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(post("/api/mixtapes/123/tracks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(put("/api/mixtapes/123/tracks/123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete_whenNotLoggedIn_thenReturnUnauthorized() throws Exception {
        this.mvc.perform(delete("/api/mixtapes/123/tracks/123"))
                .andExpect(status().isUnauthorized());
    }
}
