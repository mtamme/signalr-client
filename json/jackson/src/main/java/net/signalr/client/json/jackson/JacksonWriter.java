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
import net.signalr.client.json.JsonValue;
import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonWriter;

/**
 * Represents a Jackson based JSON writer.
 */
final class JacksonWriter implements JsonWriter {

    /**
     * The underlying JSON generator.
     */
    private final JsonGenerator _generator;

    /**
     * Initializes a new instance of the {@link JacksonWriter}.
     * 
     * @param generator The underlying JSON generator.
     */
    public JacksonWriter(final JsonGenerator generator) {
        if (generator == null) {
            throw new IllegalArgumentException("Generator must not be null");
        }

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
        try {
            _generator.writeFieldName(name);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeValue(final JsonValue value) {
        final JsonNode node = value.adapt(JsonNode.class);

        try {
            _generator.writeTree(node);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public <V> void writeObject(final V value) {
        try {
            _generator.writeObject(value);
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
