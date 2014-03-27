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

package net.signalr.client.json.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

import net.signalr.client.json.JsonValue;
import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonWriter;

/**
 * Represents a GSON based JSON writer.
 */
final class GsonWriter implements JsonWriter {

    /**
     * The GSON instance.
     */
    private final Gson _gson;

    /**
     * The underlying JSON writer.
     */
    private final com.google.gson.stream.JsonWriter _writer;

    /**
     * Initializes a new instance of the {@link GsonWriter}.
     * 
     * @param hson The GSON instance.
     * @param writer The underlying JSON writer.
     */
    public GsonWriter(final Gson gson, final com.google.gson.stream.JsonWriter writer) {
        if (gson == null) {
            throw new IllegalArgumentException("Gson must not be null");
        }
        if (writer == null) {
            throw new IllegalArgumentException("Writer must not be null");
        }

        _gson = gson;
        _writer = writer;
    }

    @Override
    public void writeBeginArray() {
        try {
            _writer.beginArray();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeEndArray() {
        try {
            _writer.endArray();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeBeginObject() {
        try {
            _writer.beginObject();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeEndObject() {
        try {
            _writer.endObject();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeName(final String name) {
        try {
            _writer.name(name);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeValue(final JsonValue value) {
        final JsonElement element = value.adapt(JsonElement.class);

        _gson.toJson(element, _writer);
    }

    @Override
    public <V> void writeObject(final V value) {
        if (value == null) {
            _gson.toJson(null, JsonNull.class, _writer);
            return;
        }
        final Class<?> valueClass = value.getClass();

        _gson.toJson(value, valueClass, _writer);
    }

    @Override
    public void writeNull() {
        try {
            _writer.nullValue();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeString(final String value) {
        try {
            _writer.value(value);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeBoolean(final boolean value) {
        try {
            _writer.value(value);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeDouble(final double value) {
        try {
            _writer.value(value);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeLong(final long value) {
        try {
            _writer.value(value);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeInt(final int value) {
        try {
            _writer.value(value);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void flush() {
        try {
            _writer.flush();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void close() {
        try {
            _writer.close();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }
}
