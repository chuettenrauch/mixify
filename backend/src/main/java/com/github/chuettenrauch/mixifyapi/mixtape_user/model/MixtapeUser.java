package com.github.chuettenrauch.mixifyapi.mixtape_user.model;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@CompoundIndex(name = "user_mixtape_unique", def = "{'user' : 1, 'mixtape': 1}", unique = true)
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
