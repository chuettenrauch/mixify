package com.github.chuettenrauch.mixifyapi.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.util.*;

import java.io.IOException;

@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final String defaultFailureUrl;

    private final RedirectStrategy redirectStrategy;

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        OAuth2AuthenticationException authenticationException = (OAuth2AuthenticationException) exception;

        UriComponents redirectUri = UriComponentsBuilder
                .fromUriString(this.defaultFailureUrl)
                .queryParam("error_code", authenticationException.getError().getErrorCode())
                .queryParam("error", authenticationException.getError().getDescription())
                .build();

        this.redirectStrategy.sendRedirect(request, response, redirectUri.toUri().toString());
    }
}
