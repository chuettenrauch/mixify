package com.github.chuettenrauch.mixifyapi.mixtape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MixtapeNotFoundException extends RuntimeException {
}
