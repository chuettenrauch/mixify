package com.github.chuettenrauch.mixifyapi.mixtape.model;

import com.github.chuettenrauch.mixifyapi.mixtape.validation.ImageUrl;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mixtape {
    @Id
    private String id;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    @ImageUrl
    private String imageUrl;

    @Size(max = 12)
    @DocumentReference(lazy = true)
    private List<Track> tracks = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    @DocumentReference(lazy = true)
    private User createdBy;

    private boolean draft = true;

    public boolean hasTrackWithId(String id) {
        return this.getTracks()
                .stream()
                .anyMatch(t -> t.getId() != null && t.getId().equals(id));
    }

    public void removeTrackWithId(String id) {
        this.setTracks(this.getTracks()
                .stream()
                .filter(t -> t.getId() == null || !t.getId().equals(id))
                .toList()
        );
    }
}
