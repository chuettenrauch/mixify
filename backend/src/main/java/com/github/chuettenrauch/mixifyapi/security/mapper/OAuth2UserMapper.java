package com.github.chuettenrauch.mixifyapi.security.mapper;

import com.github.chuettenrauch.mixifyapi.user.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserMapper {

    User mapOAuth2UserToUser(OAuth2User oAuth2User, User user);
}
