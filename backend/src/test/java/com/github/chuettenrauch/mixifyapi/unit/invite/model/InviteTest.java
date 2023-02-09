package com.github.chuettenrauch.mixifyapi.unit.invite.model;

import com.github.chuettenrauch.mixifyapi.invite.model.Invite;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class InviteTest {

    @ParameterizedTest
    @MethodSource("provideExpiredAtDates")
    void isExpired(LocalDateTime expiredAt, boolean expected) {
        // given
        Invite sut = new Invite();
        sut.setExpiredAt(expiredAt);

        // when
        boolean actual = sut.isExpired();

        // then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideExpiredAtDates() {
        return Stream.of(
                arguments(named("expired_at | now", LocalDateTime.now()), true),
                arguments(named("expired_at | expired", LocalDateTime.now().minusSeconds(1)), true),
                arguments(named("expired_at | not expired", LocalDateTime.now().plusSeconds(1)), false)
        );
    }

}