package com.github.chuettenrauch.mixifyapi.unit.security.mapper;

import com.github.chuettenrauch.mixifyapi.security.exception.NoSuchOAuth2MapperException;
import com.github.chuettenrauch.mixifyapi.security.mapper.OAuth2UserMapper;
import com.github.chuettenrauch.mixifyapi.security.mapper.OAuth2UserMapperFactory;
import com.github.chuettenrauch.mixifyapi.user.model.Provider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class OAuth2UserMapperFactoryTest {

    @Test
    void getOAuth2UserMapperByProviderName_returnsMapperWithGivenProviderName() {
        // given
        Provider provider = Provider.spotify;

        OAuth2UserMapper expectedMapper = mock(OAuth2UserMapper.class);
        when(expectedMapper.getProvider()).thenReturn(provider);

        // when
        OAuth2UserMapperFactory sut = new OAuth2UserMapperFactory(List.of(
                expectedMapper
        ));

        OAuth2UserMapper actual = sut.getOAuth2UserMapperByProviderName(provider.toString());

        // then
        assertEquals(expectedMapper, actual);
    }

    @Test
    void getOAuth2UserMapperByProviderName_throwsNoSuchOAuth2MapperExceptionIfMapperForProviderNameDoesNotExist() {
        OAuth2UserMapperFactory sut = new OAuth2UserMapperFactory(List.of());

        assertThrows(NoSuchOAuth2MapperException.class, () -> sut.getOAuth2UserMapperByProviderName("does-not-exist"));
    }
}