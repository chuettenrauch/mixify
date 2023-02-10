package com.github.chuettenrauch.mixifyapi.user.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResource {
    private String id;
    private String name;
    private String imageUrl;
    private String providerAccessToken;
    private String providerRefreshToken;

    public UserResource(String id, String name, String imageUrl, String providerAccessToken) {
        this(id, name, imageUrl, providerAccessToken, null);
    }
}
