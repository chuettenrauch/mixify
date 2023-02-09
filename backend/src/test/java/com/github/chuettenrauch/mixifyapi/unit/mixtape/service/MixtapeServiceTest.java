package com.github.chuettenrauch.mixifyapi.unit.mixtape.service;

import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.exception.UnprocessableEntityException;
import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.repository.MixtapeRepository;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import com.github.chuettenrauch.mixifyapi.mixtape_user.model.MixtapeUser;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
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
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        assertThrows(UnprocessableEntityException.class, () -> sut.save(mixtape));

        // then
        verify(mixtapeRepository, never()).save(mixtape);
    }

    @Test
    void findAllForAuthenticatedUser_whenLoggedIn_thenGetFromMixtapeUserService() {
        // given
        User user = new User();

        Mixtape mixtape1 = new Mixtape();
        Mixtape mixtape2 = new Mixtape();

        List<MixtapeUser> mixtapeUsers = List.of(
                new MixtapeUser("123", user, mixtape1),
                new MixtapeUser("123", user, mixtape2),
                new MixtapeUser("123", user, null)
        );

        List<Mixtape> expected = List.of(
                mixtape1,
                mixtape2
        );

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        when(mixtapeUserService.findAllByUser(user)).thenReturn(mixtapeUsers);

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        List<Mixtape> actual = sut.findAllForAuthenticatedUser();

        // then
        assertEquals(expected, actual);
        verify(mixtapeUserService).findAllByUser(user);
    }

    @Test
    void findAllForAuthenticatedUser_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        assertThrows(UnauthorizedException.class, sut::findAllForAuthenticatedUser);

        // then
        verify(mixtapeRepository, never()).findAllByCreatedBy(any());
    }

    @Test
    void deleteByIdForAuthenticatedUser_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        String id = "123";
        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        assertThrows(UnauthorizedException.class, () -> sut.deleteByIdForAuthenticatedUser(id));

        // then
        verify(mixtapeRepository, never()).deleteById(any());
    }

    @Test
    void deleteByIdForAuthenticatedUser_whenMixtapeDoesNotExistOrDoesNotBelongToUser_thenThrowNotFoundException() {
        // given
        String id = "123";
        User user = new User();

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.existsByIdAndCreatedBy(id, user)).thenReturn(false);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        assertThrows(NotFoundException.class, () -> sut.deleteByIdForAuthenticatedUser("123"));

        // then
        verify(mixtapeRepository, never()).deleteById(any());
    }

    @Test
    void deleteByIdForAuthenticatedUser_whenMixtapeExistsAndBelongsToUser_thenDeleteMixtape() {
        // given
        User user = new User();

        Mixtape mixtape = new Mixtape();
        mixtape.setId("123");

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.existsByIdAndCreatedBy(mixtape.getId(), user)).thenReturn(true);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        sut.deleteByIdForAuthenticatedUser(mixtape.getId());

        // then
        verify(mixtapeRepository).deleteById(mixtape.getId());
    }

    @Test
    void updateByIdForAuthenticatedUser_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        String id = "123";
        Mixtape mixtape = new Mixtape();

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        assertThrows(UnauthorizedException.class, () -> sut.updateByIdForAuthenticatedUser(id, mixtape));

        // then
        verify(mixtapeRepository, never()).save(any());
    }

    @Test
    void updateByIdForAuthenticatedUser_whenMixtapeDoesNotExistOrDoesNotBelongToLoggedInUser_thenThrowNotFoundException() {
        // given
        String id = "123";
        Mixtape mixtape = new Mixtape();
        User user = new User();

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.existsByIdAndCreatedBy(any(), eq(user))).thenReturn(false);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        assertThrows(NotFoundException.class, () -> sut.updateByIdForAuthenticatedUser(id, mixtape));

        // then
        verify(mixtapeRepository, never()).save(any());
    }

    @Test
    void updateByIdForAuthenticatedUser_whenMixtapeExists_thenEnsureIdFromUrlAndUpdateMixtape() {
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

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        Mixtape actual = sut.updateByIdForAuthenticatedUser(expectedId, mixtape);

        // then
        assertEquals(actual, expectedMixtape);
        verify(mixtapeRepository).save(expectedMixtape);
    }

    @Test
    void updateByIdForAuthenticatedUser_whenNumOfTracksExceedsMaxLimit_thenThrowConstraintViolationException() {
        // given
        String mixtapeId = "123";

        User user = new User();

        Mixtape mixtape = new Mixtape();
        mixtape.setId(mixtapeId);

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.existsByIdAndCreatedBy(mixtape.getId(), user)).thenReturn(true);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        Validator validator = mock(Validator.class);
        when(validator.validateProperty(mixtape, "tracks")).thenReturn(Set.of(
                mock(ConstraintViolationForMixtape.class)
        ));

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        assertThrows(ConstraintViolationException.class, () -> sut.updateByIdForAuthenticatedUser(mixtapeId, mixtape));

        // then
        verify(mixtapeRepository, never()).save(mixtape);
    }

    @Test
    void findByIdForAuthenticatedUser_whenNotLoggedIn_thenThrowUnauthorizedException() {
        // given
        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        assertThrows(UnauthorizedException.class, () -> sut.findByIdForAuthenticatedUser("123"));

        // then
        verify(mixtapeRepository, never()).findByIdAndCreatedBy(any(), any());
    }

    @Test
    void findByIdForAuthenticatedUser_whenMixtapeNotFoundOrDoesNotBelongToUser_thenThrowNotFoundException() {
        // given
        String id = "123";
        User user = new User();

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.findByIdAndCreatedBy(id, user)).thenReturn(Optional.empty());

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        assertThrows(NotFoundException.class, () -> sut.findByIdForAuthenticatedUser(id));

        // then
        verify(mixtapeRepository).findByIdAndCreatedBy(id, user);
    }

    @Test
    void findByIdForAuthenticatedUser_whenLoggedInAndMixtapeBelongsToUser_thenReturnMixtape() {
        // given
        String id = "123";
        User user = new User();
        Mixtape expected = new Mixtape();

        UserService userService = mock(UserService.class);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.findByIdAndCreatedBy(id, user)).thenReturn(Optional.of(expected));

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        Mixtape actual = sut.findByIdForAuthenticatedUser(id);

        // then
        assertEquals(expected, actual);
        verify(mixtapeRepository).findByIdAndCreatedBy(id, user);
    }

    @Test
    void findById_whenMixtapeNotFound_thenThrowNotFoundException() {
        // given
        String id = "123";

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.findById(id)).thenReturn(Optional.empty());

        UserService userService = mock(UserService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        assertThrows(NotFoundException.class, () -> sut.findById(id));

        // then
        verify(mixtapeRepository).findById(id);
    }

    @Test
    void findById_whenMixtapeFound_thenReturnIt() {
        // given
        String id = "123";
        Mixtape expected = new Mixtape();

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.findById(id)).thenReturn(Optional.of(expected));

        UserService userService = mock(UserService.class);
        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        Mixtape actual = sut.findById(id);

        // then
        assertEquals(expected, actual);
        verify(mixtapeRepository).findById(id);
    }

    @Test
    void existsById_whenCalled_thenDelegatesToMixtapeRepository() {
        // given
        String id = "123";
        boolean expected = true;

        UserService userService = mock(UserService.class);

        MixtapeRepository mixtapeRepository = mock(MixtapeRepository.class);
        when(mixtapeRepository.existsById(id)).thenReturn(expected);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);
        Validator validator = mock(Validator.class);

        // when
        MixtapeService sut = new MixtapeService(mixtapeRepository, userService, mixtapeUserService, validator);
        boolean actual = sut.existsById(id);

        // then
        assertEquals(expected, actual);
        verify(mixtapeRepository).existsById(id);
    }

    /**
     * This is only to overcome the issue, that it is not possible to mock classes with generic parameters
     */
    private interface ConstraintViolationForMixtape extends ConstraintViolation<Mixtape> {}
}