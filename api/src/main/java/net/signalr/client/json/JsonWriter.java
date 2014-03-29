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

import java.io.Closeable;
import java.io.Flushable;

/**
 * Defines a JSON writer.
 */
public interface JsonWriter extends Closeable, Flushable {

    /**
     * Writes a begin array.
     */
    void writeBeginArray();

    /**
     * Writes an end array.
     */
    void writeEndArray();

    /**
     * Writes a begin object.
     */
    void writeBeginObject();

    /**
     * Writes an end object.
     */
    void writeEndObject();

    /**
     * Writes a name.
     * 
     * @param name The name.
     */
    void writeName(String name);

    /**
     * Writes an element.
     * 
     * @param element The element.
     */
    void writeElement(JsonElement element);

    /**
     * Writes an object.
     * 
     * @param object The object.
     */
    <T> void writeObject(T object);

    /**
     * Writes a null value.
     */
    void writeNull();

    /**
     * Writes a string value.
     * 
     * @param value The string value.
     */
    void writeString(String value);

    /**
     * Writes a boolean value.
     * 
     * @param value The boolean value.
     */
    void writeBoolean(boolean value);

    /**
     * Writes a double value.
     * 
     * @param value The double value.
     */
    void writeDouble(double value);

    /**
     * Writes a long value.
     * 
     * @param value The long value.
     */
    void writeLong(long value);

    /**
     * Writes an integer value.
     * 
     * @param value The integer value.
     */
    void writeInt(int value);

    /**
     * Flushes the writer.
     */
    void flush();

    /**
     * Closes the writer.
     */
    void close();
}