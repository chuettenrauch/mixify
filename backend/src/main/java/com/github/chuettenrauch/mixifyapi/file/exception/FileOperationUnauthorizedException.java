package com.github.chuettenrauch.mixifyapi.file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class FileOperationUnauthorizedException extends RuntimeException {
}
