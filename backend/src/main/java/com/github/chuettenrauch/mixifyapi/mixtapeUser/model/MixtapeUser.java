package com.github.chuettenrauch.mixifyapi.mixtapeUser.model;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MixtapeUser {

    @Id
    private String id;

    @DocumentReference
    private User user;

    @DocumentReference
    private Mixtape mixtape;
}
