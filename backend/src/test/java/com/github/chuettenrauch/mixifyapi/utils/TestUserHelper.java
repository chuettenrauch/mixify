package com.github.chuettenrauch.mixifyapi.utils;

import com.github.chuettenrauch.mixifyapi.user.model.Provider;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@RequiredArgsConstructor
public class TestUserHelper {

    private final UserRepository userRepository;

    public OAuth2User createLoginUser() {
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.SPOTIFY, "user-123");

        return this.createLoginUser(user);
    }

    public OAuth2User createLoginUser(User user) {
        this.userRepository.save(user);

        return new DefaultOAuth2User(null, Map.of(
                "email", user.getEmail()
        ), "email");
    }

    public User createUser(String id) {
        User user = new User(id, null, null, null, Provider.SPOTIFY, null);

        return this.userRepository.save(user);
    }
}
