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

/**
 * Defines a JSON element.
 */
public interface JsonElement {

    /**
     * Returns a value indicating whether the element is an array.
     * 
     * @return A value indicating whether the element is an array.
     */
    boolean isArray();

    /**
     * Returns a value indicating whether the element is an object.
     * 
     * @return A value indicating whether the element is an object.
     */
    boolean isObject();

    /**
     * Returns a value indicating whether the element is a value.
     * 
     * @return A value indicating whether the element is a value.
     */
    boolean isValue();

    /**
     * Returns the element at the specified index.
     * 
     * @param index The index.
     * @return The element.
     */
    JsonElement get(int index);

    /**
     * Returns the element for the specified name.
     * 
     * @param name The name.
     * @return The element.
     */
    JsonElement get(String name);

    /**
     * Returns the element as a boolean value.
     * 
     * @param defaultValue The default value.
     * @return The boolean value.
     */
    boolean getBoolean(boolean defaultValue);

    /**
     * Returns the element as a double value.
     * 
     * @param defaultValue The default value.
     * @return The double value.
     */
    double getDouble(double defaultValue);

    /**
     * Returns the element as an integer value.
     * 
     * @param defaultValue The default value.
     * @return The integer value.
     */
    int getInt(int defaultValue);

    /**
     * Returns the element as a long value.
     * 
     * @param defaultValue The default value.
     * @return The long value.
     */
    long getLong(long defaultValue);

    /**
     * Returns the element as a string value.
     * 
     * @param defaultValue The default value.
     * @return The string value.
     */
    String getString(String defaultValue);

    /**
     * Returns the size of the element.
     * 
     * @return The size of the element.
     */
    int size();

    /**
     * Unwraps the element.
     * 
     * @param type The wrapped object type.
     * @return The wrapped object.
     */
    <T> T unwrap(Class<T> type);

    /**
     * Returns the element as an object.
     * 
     * @param type The object type.
     * @param defaultValue The default value.
     * @return The object.
     */
    <T> T toObject(Class<T> type, T defaultValue);
}
