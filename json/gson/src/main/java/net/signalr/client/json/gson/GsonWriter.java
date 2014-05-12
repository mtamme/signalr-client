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

package net.signalr.client.json.gson;

import com.google.gson.Gson;

import net.signalr.client.json.JsonElement;
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
     * The underlying writer.
     */
    private final com.google.gson.stream.JsonWriter _writer;

    /**
     * Initializes a new instance of the {@link GsonWriter}.
     * 
     * @param gson The GSON instance.
     * @param writer The underlying writer.
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
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }

        try {
            _writer.name(name);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void writeElement(final JsonElement element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null");
        }

        final com.google.gson.JsonElement object = element.unwrap(com.google.gson.JsonElement.class);

        try {
            _gson.toJson(object, _writer);
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

        final Class<?> type = object.getClass();

        try {
            _gson.toJson(object, type, _writer);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
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
