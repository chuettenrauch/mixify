package com.github.chuettenrauch.mixifyapi.integration.auth.service;

import com.github.chuettenrauch.mixifyapi.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration")
class AuthServiceTest {

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