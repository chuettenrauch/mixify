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
}
