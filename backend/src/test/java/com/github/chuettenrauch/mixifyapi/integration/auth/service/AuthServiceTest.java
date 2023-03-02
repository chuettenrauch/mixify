package com.github.chuettenrauch.mixifyapi.integration.auth.service;

import com.github.chuettenrauch.mixifyapi.auth.service.AuthService;
import com.github.chuettenrauch.mixifyapi.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthServiceTest extends AbstractIntegrationTest {

    @Test
    @WithMockUser(username = "Alvin")
    void getAuthentication_whenLoggedIn_thenReturnCurrentAuthentication() {
        AuthService sut = new AuthService();
        Authentication actual = sut.getAuthentication();

        assertNotNull(actual);
        assertEquals("Alvin", actual.getName());
    }

    @Test
    void getAuthentication_whenNotLoggedIn_thenReturnNull() {
        AuthService sut = new AuthService();
        Authentication actual = sut.getAuthentication();

        assertNull(actual);
    }
}