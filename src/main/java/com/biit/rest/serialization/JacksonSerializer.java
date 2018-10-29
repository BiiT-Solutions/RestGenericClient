package com.biit.rest.serialization;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Map;

public class JacksonSerializer {
	private static ObjectMapper objectMapper = new ObjectMapper();


	private JacksonSerializer() {
		//Utility class should not be instantiated
	}

	public static ObjectMapper getDefaultSerializer() {
		return objectMapper;
	}

	protected static ObjectMapper generateCustomSerializer(Map<Class, JsonSerializer> customSerializers) {
		ObjectMapper customObjectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		customSerializers.forEach(module::addSerializer);
		customObjectMapper.registerModule(module);
		return customObjectMapper;
	}

}
