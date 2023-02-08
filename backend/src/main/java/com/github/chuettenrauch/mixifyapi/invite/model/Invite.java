package com.github.chuettenrauch.mixifyapi.invite.model;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invite {

    @Id
    private String id;

    @DocumentReference
    private Mixtape mixtape;

    private LocalDateTime expiredAt;

}
