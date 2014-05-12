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