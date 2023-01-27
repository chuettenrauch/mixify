package com.github.chuettenrauch.mixifyapi.mixtape.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String image;

    @JsonIgnore
    @DocumentReference(lazy = true)
    private List<Track> tracks = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;
}