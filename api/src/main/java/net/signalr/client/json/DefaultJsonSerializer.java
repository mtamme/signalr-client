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

package net.signalr.client.json;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;

/**
 * Represents the default JSON serializer.
 */
public final class DefaultJsonSerializer implements JsonSerializer {

    /**
     * The JSON factory.
     */
    private final JsonFactory _factory;

    /**
     * Initializes a new instance of the {@link DefaultJsonSerializer}.
     * 
     * @param factory The JSON factory.
     */
    public DefaultJsonSerializer(final JsonFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("Factory must not be null");
        }

        _factory = factory;
    }

    @Override
    public final JsonElement fromJson(final String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null");
        }

        final StringReader input = new StringReader(text);

        try (final JsonReader reader = _factory.createReader(input)) {
            return reader.readElement();
        }
    }

    @Override
    public final <T extends JsonReadable> T fromJson(final String text, final Class<T> type) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type must not be null");
        }

        final T object;

        try {
            final Constructor<T> constructor = type.getDeclaredConstructor();

            constructor.setAccessible(true);
            object = constructor.newInstance();
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }

        final StringReader input = new StringReader(text);

        try (final JsonReader reader = _factory.createReader(input)) {
            object.readJson(reader);
        }

        return object;
    }

    @Override
    public final String toJson(final JsonWriteable object) {
        if (object == null) {
            throw new IllegalArgumentException("Object must not be null");
        }

        final StringWriter output = new StringWriter();

        try (final JsonWriter writer = _factory.createWriter(output)) {
            object.writeJson(writer);
        }

        return output.toString();
    }
}
