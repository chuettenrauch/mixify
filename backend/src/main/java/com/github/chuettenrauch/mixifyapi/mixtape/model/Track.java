package com.github.chuettenrauch.mixifyapi.mixtape.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Track {
    @Id
    private String id;
    private String name;
    private String artist;
    private String imageUrl;
    private String description;
    private String providerUri;
}
