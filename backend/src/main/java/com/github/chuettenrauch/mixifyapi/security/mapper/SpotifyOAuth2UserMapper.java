package com.github.chuettenrauch.mixifyapi.security.mapper;

import com.github.chuettenrauch.mixifyapi.security.exception.OAuth2MappingException;
import com.github.chuettenrauch.mixifyapi.user.model.Provider;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SpotifyOAuth2UserMapper implements OAuth2UserMapper {

    private static final String ATTRIBUTE_EMAIL = "email";
    private static final String ATTRIBUTE_NAME = "display_name";
    private static final String ATTRIBUTE_PROVIDER_ID = "id";
    private static final String ATTRIBUTE_IMAGES = "images";

    private static final List<String> requiredAttributes = List.of(
            ATTRIBUTE_EMAIL,
            ATTRIBUTE_NAME,
            ATTRIBUTE_PROVIDER_ID
    );

    @Override
    public Provider getProvider() {
        return Provider.spotify;
    }

    @Override
    public User mapOAuth2UserToUser(OAuth2User oAuth2User, User user) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (!this.areValidAttributes(attributes)) {
            throw new OAuth2MappingException();
        }

        user.setEmail((String) attributes.get(ATTRIBUTE_EMAIL));
        user.setName((String) attributes.get(ATTRIBUTE_NAME));

        List<Map<String, String>> images = (List<Map<String, String>>) attributes.get(ATTRIBUTE_IMAGES);
        if (!images.isEmpty()) {
            Map<String, String> image = images.get(0);

            user.setImageUrl(image.getOrDefault("url", null));
        }

        user.setProvider(this.getProvider());
        user.setProviderId((String) attributes.get(ATTRIBUTE_PROVIDER_ID));

        return user;
    }

    private boolean areValidAttributes(Map<String, Object> attributes) {
        for (String attributeName : requiredAttributes) {
            if (!attributes.containsKey(attributeName)) {
                return false;
            }

            String attributeValue = (String) attributes.get(attributeName);
            if (attributeValue == null || "".equals(attributeValue)) {
                return false;
            }
        }

        return true;
    }

}
