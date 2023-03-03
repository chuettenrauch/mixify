package com.github.chuettenrauch.mixifyapi.unit.user.serializer;

import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.serializer.UserSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserSerializerTest {

    @Autowired
    private JacksonTester<User> json;

    @Test
    void serialize_whenUserIsEmpty_thenSerializeWithDefaultUserName() throws IOException {
        // given
        User user = new User();

        // when
        JsonContent<User> actual = this.json.write(user);

        // then
        assertThat(actual).hasJsonPathStringValue("$.name");
        assertThat(actual).extractingJsonPathStringValue("$.name").isEqualTo(UserSerializer.DEFAULT_USERNAME);
    }

    @Test
    void serialize_whenUser_thenSerialize() throws IOException {
        // given
        User user = new User("123", "Alvin", "http://path/to/image", "spotify-123");

        // when
        JsonContent<User> actual = this.json.write(user);

        // then
        assertThat(actual).hasJsonPathStringValue("$.id");
        assertThat(actual).extractingJsonPathStringValue("$.id").isEqualTo(user.getId());

        assertThat(actual).hasJsonPathStringValue("$.name");
        assertThat(actual).extractingJsonPathStringValue("$.name").isEqualTo(user.getName());

        assertThat(actual).hasJsonPathStringValue("$.imageUrl");
        assertThat(actual).extractingJsonPathStringValue("$.imageUrl").isEqualTo(user.getImageUrl());

        assertThat(actual).hasJsonPathStringValue("$.spotifyId");
        assertThat(actual).extractingJsonPathStringValue("$.spotifyId").isEqualTo(user.getSpotifyId());
    }

}