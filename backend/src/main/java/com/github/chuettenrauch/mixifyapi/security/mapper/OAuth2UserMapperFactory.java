package com.github.chuettenrauch.mixifyapi.security.mapper;

import com.github.chuettenrauch.mixifyapi.security.exception.NoSuchOAuth2MapperException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuth2UserMapperFactory {
    private final Map<String, OAuth2UserMapper> oAuth2UserMapperByProvider;

    public OAuth2UserMapperFactory(List<OAuth2UserMapper> oAuth2UserMappers) {
        this.oAuth2UserMapperByProvider = oAuth2UserMappers
                .stream()
                .collect(
                        Collectors.toMap(oAuth2UserMapper -> oAuth2UserMapper.getProvider().toString(), Function.identity())
                );
    }

    public OAuth2UserMapper getOAuth2UserMapperByProviderName(String providerName) {
        if (!this.oAuth2UserMapperByProvider.containsKey(providerName)) {
            throw new NoSuchOAuth2MapperException();
        }

        return this.oAuth2UserMapperByProvider.get(providerName);
    }
}
