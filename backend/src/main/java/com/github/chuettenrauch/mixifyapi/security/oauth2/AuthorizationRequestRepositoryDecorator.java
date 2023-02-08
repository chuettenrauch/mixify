package com.github.chuettenrauch.mixifyapi.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@RequiredArgsConstructor
public class AuthorizationRequestRepositoryDecorator implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String REDIRECT_URL_PARAM = "redirect_url";
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return this.authorizationRequestRepository.loadAuthorizationRequest(request);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        String redirectUrl = request.getParameter(REDIRECT_URL_PARAM);

        if (this.isValidUrl(redirectUrl)) {
            request.getSession().setAttribute(
                    OAuth2AuthenticationSuccessHandler.REDIRECT_URL_SESSION_PARAM,
                    redirectUrl
            );
        }

        this.authorizationRequestRepository.saveAuthorizationRequest(authorizationRequest, request, response);
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }

        return true;
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.authorizationRequestRepository.removeAuthorizationRequest(request, response);
    }
}
