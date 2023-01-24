package com.github.chuettenrauch.mixifyapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Getter
public class AppProperties {
    private final OAuth2 oauth2 = new OAuth2();

    @Getter
    @Setter
    public static class OAuth2 {
        private String successRedirectUri;
    }
}
