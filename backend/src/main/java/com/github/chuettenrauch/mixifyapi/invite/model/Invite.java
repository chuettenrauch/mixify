package com.github.chuettenrauch.mixifyapi.invite.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invite {

    @Id
    private String id;

    @NotBlank
    private String mixtape;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime expiredAt;

}
