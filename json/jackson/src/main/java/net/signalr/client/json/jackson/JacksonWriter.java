/*
 * Copyright © Martin Tamme
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.signalr.client.json.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonWriter;

/**
 * Represents a Jackson based JSON writer.
 */
final class JacksonWriter implements JsonWriter {

    /**
     * The object mapper.
     */
    private final ObjectMapper _mapper;

    /**
     * The underlying generator.
     */
    private final JsonGenerator _generator;

    /**
     * Initializes a new instance of the {@link JacksonWriter}.
     * 
     * @param mapper The object mapper.
     * @param generator The underlying generator.
     */
    public JacksonWriter(final ObjectMapper mapper, final JsonGenerator generator) {
        if (mapper == null) {
            throw new IllegalArgumentException("Mapper must not be null");
        }
        if (generator == null) {
            throw new IllegalArgumentException("Generator must not be null");
        }

        _mapper = mapper;
        _generator = generator;
    }

    @Override
    public void writeBeginArray() {
        try {
            _generator.writeStartArray();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeEndArray() {
        try {
            _generator.writeEndArray();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeBeginObject() {
        try {
            _generator.writeStartObject();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeEndObject() {
        try {
            _generator.writeEndObject();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }

        try {
            _generator.writeFieldName(name);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeElement(final JsonElement element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null");
        }

        final JsonNode object = element.unwrap(JsonNode.class);

        try {
            _mapper.writeTree(_generator, object);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public <T> void writeObject(final T object) {
        if (object == null) {
            writeNull();
            return;
        }

        try {
            _mapper.writeValue(_generator, object);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeNull() {
        try {
            _generator.writeNull();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeString(final String value) {
        try {
            _generator.writeString(value);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeBoolean(final boolean value) {
        try {
            _generator.writeBoolean(value);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeDouble(final double value) {
        try {
            _generator.writeNumber(value);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeLong(final long value) {
        try {
            _generator.writeNumber(value);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeInt(final int value) {
        try {
            _generator.writeNumber(value);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void flush() {
        try {
            _generator.flush();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void close() {
        try {
            _generator.close();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }
}
