/*
 * Copyright 2014 Martin Tamme
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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonEmpty;
import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonReader;

/**
 * Represents a Jackson based JSON reader.
 */
final class JacksonReader implements JsonReader {

    /**
     * The object mapper.
     */
    private final ObjectMapper _mapper;

    /**
     * The underlying parser.
     */
    private final JsonParser _parser;

    /**
     * Initializes a new instance of the {@link JacksonReader}.
     * 
     * @param mapper The object mapper.
     * @param parser The underlying parser.
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
    public void readBeginArray() {
        final JsonToken token;

        try {
            token = _parser.nextToken();
            _parser.clearCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        if (token != JsonToken.START_ARRAY) {
            throw new JsonException("Expected START_ARRAY but was " + token);
        }
    }

    @Override
    public void readEndArray() {
        final JsonToken token;

        try {
            if (_parser.hasCurrentToken()) {
                token = _parser.getCurrentToken();
            } else {
                token = _parser.nextToken();
            }
            _parser.clearCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        if (token != JsonToken.END_ARRAY) {
            throw new JsonException("Expected END_ARRAY but was " + token);
        }
    }

    @Override
    public void readBeginObject() {
        final JsonToken token;

        try {
            token = _parser.nextToken();
            _parser.clearCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        if (token != JsonToken.START_OBJECT) {
            throw new JsonException("Expected START_OBJECT but was " + token);
        }
    }

    @Override
    public void readEndObject() {
        final JsonToken token;

        try {
            if (_parser.hasCurrentToken()) {
                token = _parser.getCurrentToken();
            } else {
                token = _parser.nextToken();
            }
            _parser.clearCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        if (token != JsonToken.END_OBJECT) {
            throw new JsonException("Expected END_OBJECT but was " + token);
        }
    }

    @Override
    public boolean read() {
        final JsonToken token;

        try {
            token = _parser.nextToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        if (token == null) {
            return false;
        }
        if (token == JsonToken.NOT_AVAILABLE) {
            return false;
        }
        if (token == JsonToken.END_ARRAY) {
            return false;
        }
        if (token == JsonToken.END_OBJECT) {
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        try {
            return _parser.getCurrentName();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public JsonElement readElement() {
        final JsonNode node;

        try {
            _parser.nextToken();
            node = _mapper.readTree(_parser);
            _parser.clearCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }
        if (node == null) {
            return JsonEmpty.INSTANCE;
        }

        return new JacksonElement(_mapper, node);
    }

    @Override
    public <T> T readObject(final Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type must not be null");
        }

        final T object;

        try {
            _parser.nextToken();
            object = _mapper.readValue(_parser, type);
            _parser.clearCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return object;
    }

    @Override
    public String readString() {
        final String value;

        try {
            _parser.nextToken();
            value = _parser.getText();
            _parser.clearCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return value;
    }

    @Override
    public boolean readBoolean() {
        final boolean value;

        try {
            _parser.nextToken();
            value = _parser.getBooleanValue();
            _parser.clearCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return value;
    }

    @Override
    public void readNull() {
        final JsonToken token;

        try {
            token = _parser.nextToken();
            _parser.clearCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        if (token != JsonToken.VALUE_NULL) {
            throw new JsonException("Expected VALUE_NULL but was " + token);
        }
    }

    @Override
    public double readDouble() {
        final double value;

        try {
            _parser.nextToken();
            value = _parser.getDoubleValue();
            _parser.clearCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return value;
    }

    @Override
    public long readLong() {
        final long value;

        try {
            _parser.nextToken();
            value = _parser.getLongValue();
            _parser.clearCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return value;
    }

    @Override
    public int readInt() {
        final int value;

        try {
            _parser.nextToken();
            value = _parser.getIntValue();
            _parser.clearCurrentToken();
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return value;
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
