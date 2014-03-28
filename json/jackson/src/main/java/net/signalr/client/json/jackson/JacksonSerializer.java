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
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonReader;
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.json.JsonWriteable;
import net.signalr.client.json.JsonWriter;

public class JacksonSerializer implements JsonSerializer {

    private final ObjectMapper _mapper;

    private final JsonFactory _factory;

    public JacksonSerializer() {
        _mapper = new ObjectMapper();
        _factory = new JsonFactory();
    }

    @Override
    public JsonReader createReader(Reader buffer) {
        final JsonParser parser;

        try {
            parser = _factory.createParser(buffer);
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return new JacksonReader(_mapper, parser);
    }

    @Override
    public JsonWriter createWriter(Writer buffer) {
        final JsonGenerator generator;

        try {
            generator = _factory.createGenerator(buffer);
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return new JacksonWriter(_mapper, generator);
    }

    @Override
    public <T extends JsonReadable> T fromJson(String json, T object) {
        final StringReader buffer = new StringReader(json);

        try (final JsonReader reader = createReader(buffer)) {
            object.readJson(reader);
        }

        return object;
    }

    @Override
    public String toJson(JsonWriteable object) {
        final StringWriter buffer = new StringWriter();

        try (final JsonWriter writer = createWriter(buffer)) {
            object.writeJson(writer);
        }

        return buffer.toString();
    }
}
