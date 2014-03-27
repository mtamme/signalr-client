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

import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.json.JsonWriteable;

/**
 * 
 */
public class JacksonSerializer implements JsonSerializer {

    private final ObjectMapper _mapper;

    private final JsonFactory _factory;

    public JacksonSerializer() {
        _mapper = new ObjectMapper();
        _factory = new JsonFactory();
    }

    @Override
    public <T extends JsonReadable> T fromJson(final String json, final T object) {
        final JsonParser parser;

        try {
            parser = _factory.createParser(json);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
        final JacksonReader reader = new JacksonReader(_mapper, parser);

        try {
            object.readJson(reader);
        } finally {
            reader.close();
        }

        return object;
    }

    @Override
    public String toJson(final JsonWriteable object) {
        final StringWriter json = new StringWriter();
        final JsonGenerator generator;

        try {
            generator = _factory.createGenerator(json);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
        final JacksonWriter writer = new JacksonWriter(generator);

        try {
            object.writeJson(writer);
        } finally {
            writer.flush();
            writer.close();
        }

        return json.toString();
    }
}
