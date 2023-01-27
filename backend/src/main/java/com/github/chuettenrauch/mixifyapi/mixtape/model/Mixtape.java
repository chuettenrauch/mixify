package com.github.chuettenrauch.mixifyapi.mixtape.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.chuettenrauch.mixifyapi.file.validation.IsOwnedByAuthenticatedUser;
import com.github.chuettenrauch.mixifyapi.user.model.User;
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
    private String title;
    private String description;

    @IsOwnedByAuthenticatedUser
    private String image;

    @JsonIgnore
    @DocumentReference(lazy = true)
    private List<Track> tracks = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    @DocumentReference(lazy = true)
    private User createdBy;
}
