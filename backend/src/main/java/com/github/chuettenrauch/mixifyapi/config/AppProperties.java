package com.github.chuettenrauch.mixifyapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Getter
public class AppProperties {
    private final OAuth2 oAuth2 = new OAuth2();
    private final Invite invite = new Invite();

    @Getter
    @Setter
    public static class OAuth2 {
        private String successRedirectUri;
        private String failureRedirectUri;
    }

    @Getter
    @Setter
    public static class Invite {
        private String expirationTime;
    }
}
