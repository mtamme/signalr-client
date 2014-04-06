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

/**
 * Defines a JSON mapper.
 */
public interface JsonMapper {

    /**
     * Converts the specified text into an element.
     * 
     * @param text The text.
     * @return The element.
     */
    JsonElement toElement(String text);

    /**
     * Converts the specified text into an object.
     * 
     * @param text The text.
     * @param type The object type.
     * @return The object.
     */
    <T extends JsonReadable> T toObject(String text, Class<T> type);

    /**
     * Converts the specified object into a text.
     * 
     * @param object The object.
     * @return The text.
     */
    String toJson(JsonWriteable object);
}
