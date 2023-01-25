package com.github.chuettenrauch.mixifyapi.unit.user.service;

import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void findByEmail_delegatesToUserRepository() {
        // given
        String email = "someone@somewhere.de";
        Optional<User> expected = Optional.of(new User());

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(email)).thenReturn(expected);

        // when
        UserService sut = new UserService(userRepository);
        Optional<User> actual = sut.findByEmail(email);

        // then
        assertEquals(expected, actual);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void save_delegatesToUserRepository() {
        // given
        User expected = new User();

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(expected)).thenReturn(expected);

        // when
        UserService sut = new UserService(userRepository);
        User actual = sut.save(expected);

        // then
        assertEquals(expected, actual);
        verify(userRepository).save(expected);
    }

}