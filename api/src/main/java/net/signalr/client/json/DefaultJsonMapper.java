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

package net.signalr.client.json;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;

/**
 * Represents the default JSON mapper.
 */
public final class DefaultJsonMapper implements JsonMapper {

    /**
     * The factory.
     */
    private final JsonFactory _factory;

    /**
     * Initializes a new instance of the {@link DefaultJsonMapper}.
     * 
     * @param factory The factory.
     */
    public DefaultJsonMapper(final JsonFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("Factory must not be null");
        }

        _factory = factory;
    }

    @Override
    public final JsonElement toElement(final String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null");
        }

        final StringReader input = new StringReader(text);

        try (final JsonReader reader = _factory.createReader(input)) {
            return reader.readElement();
        }
    }

    @Override
    public final <T extends JsonReadable> T toObject(final String text, final Class<T> type) {
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
