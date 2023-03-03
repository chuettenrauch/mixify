package com.github.chuettenrauch.mixifyapi.user.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.chuettenrauch.mixifyapi.user.model.User;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {

    public final static String DEFAULT_USERNAME = "Unknown";

    public UserSerializer() {
        this(null);
    }

    protected UserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", user.getId());
        jsonGenerator.writeStringField("name", user.getName() != null ? user.getName() : DEFAULT_USERNAME);
        jsonGenerator.writeStringField("imageUrl", user.getImageUrl());
        jsonGenerator.writeStringField("spotifyId", user.getSpotifyId());

        jsonGenerator.writeEndObject();
    }
}
