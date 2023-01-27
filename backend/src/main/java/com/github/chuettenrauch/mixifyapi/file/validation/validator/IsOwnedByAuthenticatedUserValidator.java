package com.github.chuettenrauch.mixifyapi.file.validation.validator;

import com.github.chuettenrauch.mixifyapi.file.service.FileService;
import com.github.chuettenrauch.mixifyapi.file.validation.IsOwnedByAuthenticatedUser;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@RequiredArgsConstructor
public class IsOwnedByAuthenticatedUserValidator implements ConstraintValidator<IsOwnedByAuthenticatedUser, String> {

    private final FileService fileService;

    private final UserService userService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> authenticatedUser = this.userService.getAuthenticatedUser();

        if (authenticatedUser.isEmpty()) {
            return false;
        }

        try {
            this.fileService.findFileByIdForUser(s, authenticatedUser.get());

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
