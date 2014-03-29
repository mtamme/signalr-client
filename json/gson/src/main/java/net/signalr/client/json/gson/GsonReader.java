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

import net.signalr.client.json.JsonElement;
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
     * @param hson The GSON instance.
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
            return JsonElement.NONE;
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
