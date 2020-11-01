package com.va1m.testools;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/** Encapsulate the work with json files */
public final class Json {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Json() {
    }

    /** Deserializes a json file to a given type object */
    public static <T> T deserialize(String path, Class<T> type) {
        try (InputStream is = Json.class.getClassLoader().getResourceAsStream(path)) {
            final var content = new String(is.readAllBytes());
            return OBJECT_MAPPER.readValue(content, type);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
