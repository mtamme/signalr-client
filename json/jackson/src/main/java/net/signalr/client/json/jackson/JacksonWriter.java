/*
 * Copyright © Martin Tamme
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
