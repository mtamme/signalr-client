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
     * Creates a new JSON reader.
     * 
     * @param buffer The JSON buffer.
     * @return The new JSON reader.
     */
    JsonReader createReader(Reader buffer);

    /**
     * Creates a new JSON writer.
     * 
     * @param buffer The JSON buffer.
     * @return The new JSON writer.
     */
    JsonWriter createWriter(Writer buffer);

    /**
     * Deserializes a JSON string into an object.
     * 
     * @param json The JSON string.
     * @param objectClass The object class.
     * @return The object.
     */
    <T extends JsonReadable> T fromJson(String json, Class<T> objectClass);

    /**
     * Serializes an object into a JSON string.
     * 
     * @param object The object.
     * @return The JSON string.
     */
    String toJson(JsonWriteable object);
}
