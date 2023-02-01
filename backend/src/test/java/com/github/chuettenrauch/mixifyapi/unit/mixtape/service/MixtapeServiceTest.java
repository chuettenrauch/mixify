package com.github.chuettenrauch.mixifyapi.unit.mixtape.service;

import com.github.chuettenrauch.mixifyapi.exception.BadRequestException;
import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.exception.UnprocessableEntityException;
import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MixtapeServiceTest {

    @Test
    void save_whenCalled_thenDelegateToMixtapeRepository() {
        // given
        Mixtape expected = new Mixtape();

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.save(expected)).thenReturn(expected);

        UserService userService = mock(UserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        Mixtape actual = sut.save(expected);

        // then
        assertEquals(expected, actual);
        verify(mixtapeRepository).save(expected);
    }

    @Test
    void save_whenMixtapeHasId_thenThrowUnprocessableEntityException() {
        // given
        Mixtape mixtape = new Mixtape();
        mixtape.setId("123");

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        UserService userService = mock(UserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        assertThrows(UnprocessableEntityException.class, () -> sut.save(mixtape));

        // then
        verify(mixtapeRepository, never()).save(mixtape);
    }

    @Test
    void findAllForAuthenticatedUser_whenLoggedIn_thenDelegateToMixtapeRepository() {
        // given
        User user = new User();
        List<Mixtape> expected = List.of();

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.findAllByCreatedBy(user)).thenReturn(expected);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        List<Mixtape> actual = sut.findAllForAuthenticatedUser();

        // then
        assertEquals(expected, actual);
        verify(mixtapeRepository).findAllByCreatedBy(user);
    }

    @Test
    void findAllForAuthenticatedUser_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        assertThrows(UnauthorizedException.class, sut::findAllForAuthenticatedUser);

        // then
        verify(mixtapeRepository, never()).findAllByCreatedBy(any());
    }

    @Test
    void deleteById_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        String id = "123";
        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        assertThrows(UnauthorizedException.class, () -> sut.deleteById(id));

        // then
        verify(mixtapeRepository, never()).deleteById(any());
    }

    @Test
    void deleteById_whenMixtapeDoesNotExistOrDoesNotBelongToUser_thenThrowNotFoundException() {
        // given
        String id = "123";
        User user = new User();

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.existsByIdAndCreatedBy(id, user)).thenReturn(false);

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        assertThrows(NotFoundException.class, () -> sut.deleteById("123"));

        // then
        verify(mixtapeRepository, never()).deleteById(any());
    }

    @Test
    void deleteById_whenMixtapeExistsAndBelongsToUser_thenDeleteMixtape() {
        // given
        User user = new User();

        Mixtape mixtape = new Mixtape();
        mixtape.setId("123");

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.existsByIdAndCreatedBy(mixtape.getId(), user)).thenReturn(true);

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        sut.deleteById(mixtape.getId());

        // then
        verify(mixtapeRepository).deleteById(mixtape.getId());
    }

    @Test
    void updateById_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        String id = "123";
        Mixtape mixtape = new Mixtape();

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        assertThrows(UnauthorizedException.class, () -> sut.updateById(id, mixtape));

        // then
        verify(mixtapeRepository, never()).save(any());
    }

    @Test
    void updateById_whenMixtapeDoesNotExistOrDoesNotBelongToLoggedInUser_thenThrowNotFoundException() {
        // given
        String id = "123";
        Mixtape mixtape = new Mixtape();
        User user = new User();

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.existsByIdAndCreatedBy(any(), eq(user))).thenReturn(false);

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        assertThrows(NotFoundException.class, () -> sut.updateById(id, mixtape));

        // then
        verify(mixtapeRepository, never()).save(any());
    }

    @Test
    void updateById_whenMixtapeExists_thenEnsureIdFromUrlAndUpdateMixtape() {
        // given
        String expectedId = "id-from-url";

        User user = new User();

        Mixtape mixtape = new Mixtape();
        mixtape.setId("wrong-id-in-json");

        Mixtape expectedMixtape = new Mixtape();
        expectedMixtape.setId(expectedId);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.existsByIdAndCreatedBy(expectedId, user)).thenReturn(true);
        when(mixtapeRepository.save(expectedMixtape)).thenReturn(expectedMixtape);

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        Mixtape actual = sut.updateById(expectedId, mixtape);

        // then
        assertEquals(actual, expectedMixtape);
        verify(mixtapeRepository).save(expectedMixtape);
    }

    @Test
    void updateById_whenNumOfTracksExceedsMaxLimit_thenThrowBadRequestException() {
        // given
        String mixtapeId = "123";

        User user = new User();

        Mixtape mixtape = new Mixtape();
        mixtape.setId(mixtapeId);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.existsByIdAndCreatedBy(mixtape.getId(), user)).thenReturn(true);

        Validator validator = mock(Validator.class);
        when(validator.validate(mixtape)).thenReturn(Set.of(
                mock(ConstraintViolationForMixtape.class)
        ));

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        assertThrows(BadRequestException.class, () -> sut.updateById(mixtapeId, mixtape));

        // then
        verify(mixtapeRepository, never()).save(mixtape);
    }

    @Test
    void getById_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        assertThrows(UnauthorizedException.class, () -> sut.findById("123"));

        // then
        verify(mixtapeRepository, never()).findByIdAndCreatedBy(any(), any());
    }

    @Test
    void getById_whenMixtapeNotFoundOrDoesNotBelongToUser_thenThrowNotFoundException() {
        // given
        String id = "123";
        User user = new User();

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.findByIdAndCreatedBy(id, user)).thenReturn(Optional.empty());

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        assertThrows(NotFoundException.class, () -> sut.findById(id));

        // then
        verify(mixtapeRepository).findByIdAndCreatedBy(id, user);
    }

    @Test
    void getById_whenLoggedInAndMixtapeBelongsToUser_thenReturnMixtape() {
        // given
        String id = "123";
        User user = new User();
        Mixtape expected = new Mixtape();

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.findByIdAndCreatedBy(id, user)).thenReturn(Optional.of(expected));

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, validator);
        Mixtape actual = sut.findById(id);

        // then
        assertEquals(expected, actual);
        verify(mixtapeRepository).findByIdAndCreatedBy(id, user);
    }

    /**
     * This is only to overcome the issue, that it is not possible to mock classes with generic parameters
     */
    private interface ConstraintViolationForMixtape extends ConstraintViolation<Mixtape> {}
}