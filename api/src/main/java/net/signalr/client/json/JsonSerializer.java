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

import java.io.Reader;
import java.io.Writer;

/**
 * Defines a JSON serializer.
 */
public interface JsonSerializer {

    /**
     * Creates a new reader.
     * 
     * @param input The input.
     * @return The new reader.
     */
    JsonReader createReader(Reader input);

    /**
     * Creates a new writer.
     * 
     * @param output The output.
     * @return The new writer.
     */
    JsonWriter createWriter(Writer output);

    /**
     * Deserializes a text into an element.
     * 
     * @param text The text.
     * @return The element.
     */
    JsonElement fromJson(String text);

    /**
     * Deserializes a text into an object.
     * 
     * @param text The text.
     * @param type The object type.
     * @return The object.
     */
    <T extends JsonReadable> T fromJson(String text, Class<T> type);

    /**
     * Serializes an object into a text.
     * 
     * @param object The object.
     * @return The text.
     */
    String toJson(JsonWriteable object);
}
