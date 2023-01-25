package com.github.chuettenrauch.mixifyapi.unit.user.service;

import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Test
    void findByEmail_returnsOptionalWithUserIfUserExists() {
        // given
        String email = "someone@somewhere.de";
        User expected = new User();

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expected));

        // when
        UserService sut = new UserService(userRepository);
        Optional<User> actual = sut.findByEmail(email);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void findByEmail_returnsOptionalOfNullIfUserDoesNotExist() {
        // given
        String email = "someone@somewhere.de";

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        UserService sut = new UserService(userRepository);
        Optional<User> actual = sut.findByEmail(email);

        // then
        assertFalse(actual.isPresent());
        assertThrows(NoSuchElementException.class, actual::get);
    }

}