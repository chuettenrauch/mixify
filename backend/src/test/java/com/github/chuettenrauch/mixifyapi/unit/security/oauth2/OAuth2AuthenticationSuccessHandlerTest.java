package com.github.chuettenrauch.mixifyapi.unit.security.oauth2;

import com.github.chuettenrauch.mixifyapi.security.oauth2.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;

import java.io.IOException;

import static org.mockito.Mockito.*;

class OAuth2AuthenticationSuccessHandlerTest {

    @Test
    void onAuthenticationSuccess_whenRedirectUrlInSession_thenRedirectToIt() throws ServletException, IOException {
        // given
        String redirectUrl = "http://redirect";

        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute(OAuth2AuthenticationSuccessHandler.REDIRECT_URL_SESSION_PARAM))
                .thenReturn(redirectUrl);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(httpSession);

        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);

        RedirectStrategy redirectStrategy = mock(RedirectStrategy.class);

        // when
        OAuth2AuthenticationSuccessHandler sut = new OAuth2AuthenticationSuccessHandler("/does-not-matter");
        sut.setRedirectStrategy(redirectStrategy);

        sut.onAuthenticationSuccess(request, response, authentication);

        // then
        verify(redirectStrategy).sendRedirect(request, response, redirectUrl);
        verify(httpSession).removeAttribute(OAuth2AuthenticationSuccessHandler.REDIRECT_URL_SESSION_PARAM);
    }

    @Test
    void onAuthenticationSuccess_whenRedirectUrlNotInSession_thenRedirectToDefaultTargetUrl() throws ServletException, IOException {
        // given
        String defaultTargetUrl = "http://default";

        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute(OAuth2AuthenticationSuccessHandler.REDIRECT_URL_SESSION_PARAM))
                .thenReturn(null);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(httpSession);

        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);

        RedirectStrategy redirectStrategy = mock(RedirectStrategy.class);

        // when
        OAuth2AuthenticationSuccessHandler sut = new OAuth2AuthenticationSuccessHandler(defaultTargetUrl);
        sut.setRedirectStrategy(redirectStrategy);

        sut.onAuthenticationSuccess(request, response, authentication);

        // then
        verify(redirectStrategy).sendRedirect(request, response, defaultTargetUrl);
    }
}