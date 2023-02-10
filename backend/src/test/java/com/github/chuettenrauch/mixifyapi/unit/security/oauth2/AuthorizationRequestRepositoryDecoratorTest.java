package com.github.chuettenrauch.mixifyapi.unit.security.oauth2;

import com.github.chuettenrauch.mixifyapi.security.oauth2.AuthorizationRequestRepositoryDecorator;
import com.github.chuettenrauch.mixifyapi.security.oauth2.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;


import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

class AuthorizationRequestRepositoryDecoratorTest {

    @Test
    void loadAuthorizationRequest_whenCalled_thenDelegateToDecoratedAuthorizationRequestRepository() {
        // given
        OAuth2AuthorizationRequest expected = this.createOAuth2AuthorizationRequest();
        HttpServletRequest request = mock(HttpServletRequest.class);

        AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository = mock(AuthorizationRequestRepositoryStub.class);
        when(authorizationRequestRepository.loadAuthorizationRequest(request)).thenReturn(expected);

        // when
        AuthorizationRequestRepositoryDecorator sut = new AuthorizationRequestRepositoryDecorator(authorizationRequestRepository);
        OAuth2AuthorizationRequest actual = sut.loadAuthorizationRequest(request);

        // then
        assertEquals(expected, actual);
        verify(authorizationRequestRepository).loadAuthorizationRequest(request);
    }

    @Test
    void saveAuthorizationRequest_whenRedirectUrlIsInRequest_thenSaveToSessionAndDelegateToDecoratedAuthorizationRequestRepository() {
        // given
        String redirectUrl = "http://some-url";
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = this.createOAuth2AuthorizationRequest();

        HttpSession httpSession = mock(HttpSession.class);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("redirect_url")).thenReturn(redirectUrl);
        when(request.getSession()).thenReturn(httpSession);

        HttpServletResponse response = mock(HttpServletResponse.class);

        AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository = mock(AuthorizationRequestRepositoryStub.class);

        // when
        AuthorizationRequestRepositoryDecorator sut = new AuthorizationRequestRepositoryDecorator(authorizationRequestRepository);
        sut.saveAuthorizationRequest(oAuth2AuthorizationRequest, request, response);

        // then
        verify(httpSession).setAttribute(OAuth2AuthenticationSuccessHandler.REDIRECT_URL_SESSION_PARAM, redirectUrl);
        verify(authorizationRequestRepository).saveAuthorizationRequest(oAuth2AuthorizationRequest, request, response);
    }

    @ParameterizedTest
    @MethodSource("providerInvalidRedirectUrl")
    void saveAuthorizationRequest_whenRedirectUrlInvalid_thenOnlyDelegateToDecoratedAuthorizationRequestRepository(
            String redirectUrl
    ) {
        // given
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = this.createOAuth2AuthorizationRequest();

        HttpSession httpSession = mock(HttpSession.class);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("redirect_url")).thenReturn(redirectUrl);
        when(request.getSession()).thenReturn(httpSession);

        HttpServletResponse response = mock(HttpServletResponse.class);

        AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository = mock(AuthorizationRequestRepositoryStub.class);

        // when
        AuthorizationRequestRepositoryDecorator sut = new AuthorizationRequestRepositoryDecorator(authorizationRequestRepository);
        sut.saveAuthorizationRequest(oAuth2AuthorizationRequest, request, response);

        // then
        verify(httpSession, never()).setAttribute(any(), any());
        verify(authorizationRequestRepository).saveAuthorizationRequest(oAuth2AuthorizationRequest, request, response);
    }

    private static Stream<Arguments> providerInvalidRedirectUrl() {
        return Stream.of(
                arguments(named("redirect_url | missing", null)),
                arguments(named("redirect_url | empty", "")),
                arguments(named("redirect_url | invalid url", "some-invalid-url"))
        );
    }

    @Test
    void removeAuthorizationRequest_whenCalled_thenDelegateToDecoratedAuthorizationRequestRepository() {
        // given
        OAuth2AuthorizationRequest expected = this.createOAuth2AuthorizationRequest();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository = mock(AuthorizationRequestRepositoryStub.class);
        when(authorizationRequestRepository.removeAuthorizationRequest(request, response)).thenReturn(expected);

        // when
        AuthorizationRequestRepositoryDecorator sut = new AuthorizationRequestRepositoryDecorator(authorizationRequestRepository);
        OAuth2AuthorizationRequest actual = sut.removeAuthorizationRequest(request, response);

        // then
        assertEquals(expected, actual);
        verify(authorizationRequestRepository).removeAuthorizationRequest(request, response);
    }

    private OAuth2AuthorizationRequest createOAuth2AuthorizationRequest() {
        return OAuth2AuthorizationRequest
                .authorizationCode()
                .authorizationUri("some-uri")
                .clientId("client-id")
                .state("state123")
                .redirectUri("http://localhost")
                .build();
    }

    /**
     * This is only to overcome the issue, that it is not possible to mock classes with generic parameters
     */
    private interface AuthorizationRequestRepositoryStub extends AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    }
}