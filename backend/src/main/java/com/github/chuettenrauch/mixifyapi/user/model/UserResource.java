package com.github.chuettenrauch.mixifyapi.user.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResource {
    private String id;
    private String name;
    private String imageUrl;
    private String accessToken;
    private String refreshToken;

    public UserResource(String id, String name, String imageUrl, String accessToken) {
        this(id, name, imageUrl, accessToken, null);
    }
}
