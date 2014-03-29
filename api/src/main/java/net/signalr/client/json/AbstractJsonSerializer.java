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
 * Represents an abstract JSON serializer.
 */
public abstract class AbstractJsonSerializer implements JsonSerializer {

    @Override
    public final JsonValue fromJson(final String json) {
        final StringReader buffer = new StringReader(json);

        try (final JsonReader reader = createReader(buffer)) {
            return reader.readValue();
        }
    }

    @Override
    public final <T extends JsonReadable> T fromJson(final String json, final Class<T> objectClass) {
        final T object;

        try {
            final Constructor<T> constructor = objectClass.getDeclaredConstructor();

            constructor.setAccessible(true);
            object = constructor.newInstance();
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }

        final StringReader buffer = new StringReader(json);

        try (final JsonReader reader = createReader(buffer)) {
            object.readJson(reader);
        }

        return object;
    }

    @Override
    public final String toJson(final JsonWriteable object) {
        final StringWriter buffer = new StringWriter();

        try (final JsonWriter writer = createWriter(buffer)) {
            object.writeJson(writer);
        }

        return buffer.toString();
    }
}
