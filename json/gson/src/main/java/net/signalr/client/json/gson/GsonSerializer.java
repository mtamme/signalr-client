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

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Modifier;

import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonReader;
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.json.JsonWriteable;
import net.signalr.client.json.JsonWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonSerializer implements JsonSerializer {

    private final Gson _gson;

    public GsonSerializer() {
        _gson = build();
    }

    private static Gson build() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.excludeFieldsWithModifiers(Modifier.STATIC);

        return gsonBuilder.create();
    }

    @Override
    public JsonReader createReader(final Reader buffer) {
        final com.google.gson.stream.JsonReader reader;

        try {
            reader = new com.google.gson.stream.JsonReader(buffer);
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return new GsonReader(_gson, reader);
    }

    @Override
    public JsonWriter createWriter(final Writer buffer) {
        final com.google.gson.stream.JsonWriter writer;

        try {
            writer = new com.google.gson.stream.JsonWriter(buffer);
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return new GsonWriter(_gson, writer);
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