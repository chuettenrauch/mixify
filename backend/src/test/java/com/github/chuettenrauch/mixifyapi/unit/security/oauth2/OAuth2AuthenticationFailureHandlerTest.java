package com.github.chuettenrauch.mixifyapi.unit.security.oauth2;

import com.github.chuettenrauch.mixifyapi.security.oauth2.OAuth2AuthenticationFailureHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.RedirectStrategy;

import java.io.IOException;

import static org.mockito.Mockito.*;

class OAuth2AuthenticationFailureHandlerTest {

    @Test
    void onAuthenticationFailure_whenCalled_thenAppendErrorToFailureUrl() throws IOException {
        // given
        String givenFailureUrl = "http://path/to/failure/url";
        RedirectStrategy redirectStrategy = mock(RedirectStrategy.class);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        OAuth2AuthenticationException exception = mock(OAuth2AuthenticationException.class);
        when(exception.getError()).thenReturn(new OAuth2Error("some-code", "Some Message", ""));

        String expectedRedirectUrl = "http://path/to/failure/url?error_code=some-code&error=Some%20Message";

        // when
        OAuth2AuthenticationFailureHandler sut = new OAuth2AuthenticationFailureHandler(givenFailureUrl, redirectStrategy);
        sut.onAuthenticationFailure(request, response, exception);

        // then
        verify(redirectStrategy).sendRedirect(request, response, expectedRedirectUrl);
    }

}