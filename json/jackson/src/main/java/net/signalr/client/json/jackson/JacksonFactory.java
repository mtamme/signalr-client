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
    public JsonReader createReader(final Reader input) {
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
    public JsonWriter createWriter(final Writer output) {
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
