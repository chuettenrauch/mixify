package com.github.chuettenrauch.mixifyapi.security;

import com.github.chuettenrauch.mixifyapi.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppProperties appProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/oauth2/**").permitAll()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(config -> config
                                .baseUri("/oauth2/code/*")
                        )
                        .successHandler(authenticationSuccessHandler(
                                appProperties.getOauth2().getSuccessRedirectUri()
                        ))
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .build();
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler(String redirectUri) {
        return new SimpleUrlAuthenticationSuccessHandler(redirectUri);
    }
}
