package com.biit.rest.serialization;

/*-
 * #%L
 * Rest Generic Client
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
