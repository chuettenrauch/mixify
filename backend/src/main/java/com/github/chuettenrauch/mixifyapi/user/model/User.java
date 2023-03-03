package com.github.chuettenrauch.mixifyapi.user.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.chuettenrauch.mixifyapi.user.serializer.UserSerializer;
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
@JsonSerialize(using = UserSerializer.class)
public class User {
    @Id
    private String id;
    private String name;
    private String imageUrl;

    @Indexed(unique = true)
    private String spotifyId;
}
