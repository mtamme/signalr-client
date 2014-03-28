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

import net.signalr.client.json.AbstractJsonSerializer;
import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonReader;
import net.signalr.client.json.JsonWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a Jackson based JSON serializer.
 */
public final class JacksonSerializer extends AbstractJsonSerializer {

    /**
     * The object mapper.
     */
    private final ObjectMapper _mapper;

    /**
     * The JSON factory.
     */
    private final JsonFactory _factory;

    /**
     * Initializes a new instance of the {@link JacksonSerializer} class.
     */
    public JacksonSerializer() {
        _mapper = new ObjectMapper();
        _factory = new JsonFactory();
    }

    @Override
    public JsonReader createReader(final Reader buffer) {
        final JsonParser parser;

        try {
            parser = _factory.createParser(buffer);
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return new JacksonReader(_mapper, parser);
    }

    @Override
    public JsonWriter createWriter(final Writer buffer) {
        final JsonGenerator generator;

        try {
            generator = _factory.createGenerator(buffer);
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return new JacksonWriter(_mapper, generator);
    }
}
