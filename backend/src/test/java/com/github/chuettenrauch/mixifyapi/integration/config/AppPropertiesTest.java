package com.github.chuettenrauch.mixifyapi.integration.config;

import com.github.chuettenrauch.mixifyapi.config.AppProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        classes = { AppPropertiesTest.TestApplication.class },
        properties = {
            "app.o-auth2.success-redirect-uri=/success",
            "app.o-auth2.failure-redirect-uri=/failure",
            "app.invite.expiration-time=PT1H",
        }
)
class AppPropertiesTest {

    @Autowired
    private AppProperties appProperties;

    @Test
    void correctlyBindsPropertiesToAppConfig() {
        AppProperties.OAuth2 oauth2 = appProperties.getOAuth2();
        AppProperties.Invite invite = appProperties.getInvite();

        assertEquals("/success", oauth2.getSuccessRedirectUri());
        assertEquals("/failure", oauth2.getFailureRedirectUri());
        assertEquals("PT1H", invite.getExpirationTime());
    }

    @EnableConfigurationProperties(AppProperties.class)
    public static class TestApplication {}
}
