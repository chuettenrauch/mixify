package com.github.chuettenrauch.mixifyapi.audit.provider;

import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuditorProviderTest {

    @Test
    void getCurrentAuditor_whenLoggedIn_thenReturnUserId() {
        // given
        User user = new User();
        user.setId("123");

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        // when
        AuditorProvider sut = new AuditorProvider(userService);
        Optional<String> actual = sut.getCurrentAuditor();

        // then
        assertTrue(actual.isPresent());
        assertEquals(user.getId(), actual.get());
    }

    @Test
    void getCurrentAuditor_whenNotLoggedIn_thenReturnEmpty() {
        // given
        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        // when
        AuditorProvider sut = new AuditorProvider(userService);
        Optional<String> actual = sut.getCurrentAuditor();

        // then
        assertTrue(actual.isEmpty());
    }

}