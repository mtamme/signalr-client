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
     * @return The object.
     */
    <T> T toObject(Class<T> type);

    /**
     * Returns a string representation of the element.
     * 
     * @return A string representation of the element.
     */
    String toString();
}
