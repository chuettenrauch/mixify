package com.github.chuettenrauch.mixifyapi.security.config;

import com.github.chuettenrauch.mixifyapi.config.AppProperties;
import com.github.chuettenrauch.mixifyapi.security.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppProperties appProperties;
    private final OAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(config -> config
                                .userService(oAuth2UserService)
                        )
                        .redirectionEndpoint(config -> config
                                .baseUri("/oauth2/code/*")
                        )
                        .successHandler(authenticationSuccessHandler(
                                appProperties.getOAuth2().getSuccessRedirectUri()
                        ))
                        .failureHandler(authenticationFailureHandler(
                                appProperties.getOAuth2().getFailureRedirectUri()
                        ))
                )
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(authenticationEntryPoint())
                )
                .build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler(String redirectUri) {
        return new SimpleUrlAuthenticationSuccessHandler(redirectUri);
    }

    private AuthenticationFailureHandler authenticationFailureHandler(String redirectUri) {
        return new SimpleUrlAuthenticationFailureHandler(redirectUri);
    }
}
