package com.biit.rest.serialization;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Map;

public final class JacksonSerializer {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private JacksonSerializer() {
        // Utility class should not be instantiated
    }

    public static ObjectMapper getDefaultSerializer() {
        return objectMapper;
    }

    public static ObjectMapper generateCustomSerializer(Map<Class<?>, JsonSerializer> customSerializers) {
        final ObjectMapper customObjectMapper = new ObjectMapper();
        final SimpleModule module = new SimpleModule();
        customSerializers.forEach(module::addSerializer);
        customObjectMapper.registerModule(module);
        return customObjectMapper;
    }

}
