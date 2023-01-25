package com.github.chuettenrauch.mixifyapi.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String email;
    private String name;
    private String imageUrl;
    private Provider provider;
    private String providerId;
}
