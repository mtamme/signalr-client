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

/**
 * Defines a JSON reader.
 */
public interface JsonReader extends Closeable {

    /**
     * Reads a begin array.
     */
    void readBeginArray();

    /**
     * Reads an end array.
     */
    void readEndArray();

    /**
     * Reads a begin object.
     */
    void readBeginObject();

    /**
     * Reads an end object.
     */
    void readEndObject();

    /**
     * Reads the next element.
     * 
     * @return A value indicating whether a next element is available.
     */
    boolean read();

    /**
     * Returns the current name.
     * 
     * @return The current name.
     */
    String getName();

    /**
     * Reads an element.
     * 
     * @return The element.
     */
    JsonElement readElement();

    /**
     * Reads an object.
     * 
     * @param type The object type.
     * @return The object.
     */
    <T> T readObject(Class<T> type);

    /**
     * Reads a null value.
     */
    void readNull();

    /**
     * Reads a string value.
     * 
     * @return The string value.
     */
    String readString();

    /**
     * Reads a boolean value.
     * 
     * @return The boolean value.
     */
    boolean readBoolean();

    /**
     * Reads a double value.
     * 
     * @return The double value.
     */
    double readDouble();

    /**
     * Reads a long value.
     * 
     * @return The long value.
     */
    long readLong();

    /**
     * Reads an integer value.
     * 
     * @return The integer value.
     */
    int readInt();

    /**
     * Closes the reader.
     */
    void close();
}