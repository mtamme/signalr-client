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
