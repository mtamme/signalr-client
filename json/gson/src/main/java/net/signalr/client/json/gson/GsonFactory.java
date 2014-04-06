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
import java.io.Writer;

import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonReader;
import net.signalr.client.json.JsonWriter;

import com.google.gson.Gson;

/**
 * Represents a GSON based JSON factory.
 */
public final class GsonFactory implements JsonFactory {

    /**
     * The GSON instance.
     */
    private final Gson _gson;

    /**
     * Initializes a new instance of the {@link GsonFactory} class.
     */
    public GsonFactory() {
        this(new Gson());
    }

    /**
     * Initializes a new instance of the {@link GsonFactory} class.
     * 
     * @param gson The GSON instance.
     */
    public GsonFactory(final Gson gson) {
        if (gson == null) {
            throw new IllegalArgumentException("Gson must not be null");
        }

        _gson = gson;
    }

    @Override
    public JsonReader createReader(final Reader input) {
        if (input == null) {
            throw new IllegalArgumentException("Input must not be null");
        }

        final com.google.gson.stream.JsonReader reader;

        try {
            reader = new com.google.gson.stream.JsonReader(input);
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return new GsonReader(_gson, reader);
    }

    @Override
    public JsonWriter createWriter(final Writer output) {
        if (output == null) {
            throw new IllegalArgumentException("Output must not be null");
        }

        final com.google.gson.stream.JsonWriter writer;

        try {
            writer = new com.google.gson.stream.JsonWriter(output);
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return new GsonWriter(_gson, writer);
    }
}