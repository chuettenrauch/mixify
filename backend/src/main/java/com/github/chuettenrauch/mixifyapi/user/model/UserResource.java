package com.github.chuettenrauch.mixifyapi.user.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class UserResource {
    @NonNull private String name;
    private String imageUrl;
    @NonNull private String providerAccessToken;
    private String providerRefreshToken;

    public UserResource(String name, String imageUrl, String providerAccessToken) {
        this(name, imageUrl, providerAccessToken, null);
    }
}
