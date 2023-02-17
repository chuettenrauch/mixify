package com.github.chuettenrauch.mixifyapi.mixtape.model;

import com.github.chuettenrauch.mixifyapi.mixtape.validation.ImageUrl;
import com.github.chuettenrauch.mixifyapi.mixtape.validation.SpotifyUri;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String name;

    @NotBlank
    private String artist;

    @NotBlank
    @ImageUrl
    private String imageUrl;

    private String description;

    @NotBlank
    @SpotifyUri
    private String spotifyUri;
}
