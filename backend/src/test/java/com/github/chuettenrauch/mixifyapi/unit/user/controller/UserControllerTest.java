package com.github.chuettenrauch.mixifyapi.unit.user.controller;

import com.github.chuettenrauch.mixifyapi.security.config.SecurityConfig;
import com.github.chuettenrauch.mixifyapi.user.controller.UserController;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ImportAutoConfiguration(classes = SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    void me_returnsUnauthorizedIfNotLoggedIn() throws Exception {
        this.mvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_returnsUnauthorizedIfNotLoggedIn() throws Exception {
        this.mvc.perform(post("/api/users/logout"))
                .andExpect(status().isUnauthorized());
    }

}
