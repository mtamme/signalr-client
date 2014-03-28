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

import net.signalr.client.json.JsonValue;
import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonReader;
import net.signalr.client.json.JsonToken;

/**
 * Represents a GSON based JSON reader.
 */
final class GsonReader implements JsonReader {

    /**
     * The GSON instance.
     */
    private final Gson _gson;

    /**
     * The underlying JSON reader.
     */
    private final com.google.gson.stream.JsonReader _reader;

    /**
     * Initializes a new instance of the {@link GsonReader}.
     * 
     * @param hson The GSON instance.
     * @param reader The underlying JSON reader.
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
    public JsonToken getToken() {
        final com.google.gson.stream.JsonToken token;

        try {
            token = _reader.peek();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        switch (token) {
        case BEGIN_ARRAY:
            return JsonToken.BEGIN_ARRAY;
        case END_ARRAY:
            return JsonToken.END_ARRAY;
        case BEGIN_OBJECT:
            return JsonToken.BEGIN_OBJECT;
        case END_OBJECT:
            return JsonToken.END_OBJECT;
        case NAME:
            return JsonToken.NAME;
        case STRING:
            return JsonToken.STRING;
        case NUMBER:
            return JsonToken.NUMBER;
        case BOOLEAN:
            return JsonToken.BOOLEAN;
        case NULL:
            return JsonToken.NULL;
        case END_DOCUMENT:
            return JsonToken.END_DOCUMENT;
        default:
            return JsonToken.NONE;
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
    public JsonValue readValue() {
        final JsonElement element;

        try {
            element = _gson.fromJson(_reader, JsonElement.class);
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return new GsonValue(_gson, element);
    }

    @Override
    public <V> V readObject(final Class<V> valueClass) {
        if (valueClass == null) {
            throw new IllegalArgumentException("Value class must not be null");
        }

        try {
            return _gson.fromJson(_reader, valueClass);
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
