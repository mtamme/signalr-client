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

import java.io.Reader;
import java.io.Writer;

import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonReader;
import net.signalr.client.json.JsonWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a Jackson based JSON factory.
 */
public final class JacksonFactory implements JsonFactory {

    /**
     * The object mapper.
     */
    private final ObjectMapper _mapper;

    /**
     * Initializes a new instance of the {@link JacksonFactory} class.
     */
    public JacksonFactory() {
        this(new ObjectMapper());
    }

    /**
     * Initializes a new instance of the {@link JacksonFactory} class.
     * 
     * @param mapper The object mapper.
     */
    public JacksonFactory(final ObjectMapper mapper) {
        if (mapper == null) {
            throw new IllegalArgumentException("Mapper must not be null");
        }

        _mapper = mapper;
    }

    @Override
    public JsonReader newReader(final Reader input) {
        if (input == null) {
            throw new IllegalArgumentException("Input must not be null");
        }

        final JsonParser parser;

        try {
            final com.fasterxml.jackson.core.JsonFactory factory = _mapper.getFactory();

            parser = factory.createParser(input);
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return new JacksonReader(_mapper, parser);
    }

    @Override
    public JsonWriter newWriter(final Writer output) {
        if (output == null) {
            throw new IllegalArgumentException("Output must not be null");
        }

        final JsonGenerator generator;

        try {
            final com.fasterxml.jackson.core.JsonFactory factory = _mapper.getFactory();

            generator = factory.createGenerator(output);
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return new JacksonWriter(_mapper, generator);
    }
}
