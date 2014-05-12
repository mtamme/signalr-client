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
import net.signalr.client.json.JsonEmpty;
import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonReader;

/**
 * Represents a GSON based JSON reader.
 */
final class GsonReader implements JsonReader {

    /**
     * The GSON instance.
     */
    private final Gson _gson;

    /**
     * The underlying reader.
     */
    private final com.google.gson.stream.JsonReader _reader;

    /**
     * Initializes a new instance of the {@link GsonReader}.
     * 
     * @param gson The GSON instance.
     * @param reader The underlying reader.
     */
    public GsonReader(final Gson gson, final com.google.gson.stream.JsonReader reader) {
        if (gson == null) {
            throw new IllegalArgumentException("Gson must not be null");
        }
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }

        _gson = gson;
        _reader = reader;
    }

    @Override
    public void readBeginArray() {
        try {
            _reader.beginArray();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void readEndArray() {
        try {
            _reader.endArray();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void readBeginObject() {
        try {
            _reader.beginObject();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void readEndObject() {
        try {
            _reader.endObject();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public boolean read() {
        try {
            return _reader.hasNext();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public String getName() {
        try {
            return _reader.nextName();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public JsonElement readElement() {
        final com.google.gson.JsonElement element;

        try {
            element = _gson.fromJson(_reader, com.google.gson.JsonElement.class);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
        if (element == null) {
            return JsonEmpty.INSTANCE;
        }

        return new GsonElement(_gson, element);
    }

    @Override
    public <T> T readObject(final Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type must not be null");
        }

        try {
            return _gson.fromJson(_reader, type);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public String readString() {
        try {
            return _reader.nextString();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public boolean readBoolean() {
        try {
            return _reader.nextBoolean();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void readNull() {
        try {
            _reader.nextNull();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public double readDouble() {
        try {
            return _reader.nextDouble();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public long readLong() {
        try {
            return _reader.nextLong();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public int readInt() {
        try {
            return _reader.nextInt();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void close() {
        try {
            _reader.close();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }
}
