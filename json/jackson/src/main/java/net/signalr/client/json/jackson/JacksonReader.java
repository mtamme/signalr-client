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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonReader;
import net.signalr.client.json.JsonToken;

/**
 * Represents a Jackson based JSON reader.
 */
final class JacksonReader implements JsonReader {

    /**
     * The object mapper.
     */
    private final ObjectMapper _mapper;

    /**
     * The underlying JSON parser.
     */
    private final JsonParser _parser;

    /**
     * Initializes a new instance of the {@link JacksonReader}.
     * 
     * @param mapper The object mapper.
     * @param parser The underlying JSON parser.
     */
    public JacksonReader(final ObjectMapper mapper, final JsonParser parser) {
        if (mapper == null) {
            throw new IllegalArgumentException("Mapper must not be null");
        }
        if (parser == null) {
            throw new IllegalArgumentException("Parser must not be null");
        }

        _mapper = mapper;
        _parser = parser;
    }

    @Override
    public void beginArray() {
        try {
            _parser.nextToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void endArray() {
        try {
            _parser.nextToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void beginObject() {
        try {
            _parser.nextToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void endObject() {
        try {
            _parser.nextToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public boolean hasNext() {
        final com.fasterxml.jackson.core.JsonToken token;

        try {
            token = _parser.nextToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        if (token == null) {
            return false;
        }
        if (token == com.fasterxml.jackson.core.JsonToken.NOT_AVAILABLE) {
            return false;
        }
        if (token == com.fasterxml.jackson.core.JsonToken.END_ARRAY) {
            return false;
        }
        if (token == com.fasterxml.jackson.core.JsonToken.END_OBJECT) {
            return false;
        }

        return true;
    }

    @Override
    public JsonToken peek() {
        final com.fasterxml.jackson.core.JsonToken token;

        try {
            token = _parser.getCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        if (token == null) {
            return JsonToken.END_DOCUMENT;
        }

        switch (token) {
        case START_ARRAY:
            return JsonToken.BEGIN_ARRAY;
        case END_ARRAY:
            return JsonToken.END_ARRAY;
        case START_OBJECT:
            return JsonToken.BEGIN_OBJECT;
        case END_OBJECT:
            return JsonToken.END_OBJECT;
        case FIELD_NAME:
            return JsonToken.NAME;
        case VALUE_STRING:
            return JsonToken.STRING;
        case VALUE_NUMBER_INT:
        case VALUE_NUMBER_FLOAT:
            return JsonToken.NUMBER;
        case VALUE_TRUE:
        case VALUE_FALSE:
            return JsonToken.BOOLEAN;
        case VALUE_NULL:
            return JsonToken.NULL;
        case NOT_AVAILABLE:
            return JsonToken.END_DOCUMENT;
        default:
            return JsonToken.UNKNOWN;
        }
    }

    @Override
    public String nextName() {
        try {
            return _parser.getCurrentName();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public JsonElement nextElement() {
        try {
            final JsonNode node = _mapper.readTree(_parser);

            return new JacksonElement(_mapper, node);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public <V> V nextValue(final Class<V> valueClass) {
        try {
            return _mapper.readTree(_parser);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public String nextString() {
        try {
            _parser.nextToken();
            return _parser.getText();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public boolean nextBoolean() {
        try {
            _parser.nextToken();
            return _parser.getBooleanValue();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void nextNull() {
        try {
            _parser.nextToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public double nextDouble() {
        try {
            _parser.nextToken();
            return _parser.getDoubleValue();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public long nextLong() {
        try {
            _parser.nextToken();
            return _parser.getLongValue();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public int nextInt() {
        try {
            _parser.nextToken();
            return _parser.getIntValue();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void close() {
        try {
            _parser.close();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }
}
