package com.github.chuettenrauch.mixifyapi.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REDIRECT_URL_SESSION_PARAM = "authenticationSuccessRedirectUrl";

    public OAuth2AuthenticationSuccessHandler(String defaultTargetUrl) {
        super(defaultTargetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String redirectUrl = (String) session.getAttribute(REDIRECT_URL_SESSION_PARAM);

        if (redirectUrl == null) {
            return super.determineTargetUrl(request, response);
        }

        session.removeAttribute(REDIRECT_URL_SESSION_PARAM);

        return redirectUrl;
    }
}
