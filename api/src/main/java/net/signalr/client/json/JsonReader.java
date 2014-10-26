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
     * Skips a value.
     */
    void skipValue();

    /**
     * Closes the reader.
     */
    void close();
}