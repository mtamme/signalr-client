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
 * Defines a JSON value.
 */
public interface JsonValue {

    /**
     * The JSON none element.
     */
    public static final JsonValue NULL = new JsonNull();

    /**
     * Returns a JSON value at the specified index.
     * 
     * @param index The index.
     * @return The JSON element.
     */
    JsonValue get(int index);

    /**
     * Returns a JSON value for the specified name.
     * 
     * @param name The name.
     * @return The JSON element.
     */
    JsonValue get(String name);

    /**
     * Returns the value as a boolean.
     * 
     * @param defaultValue
     * @return
     */
    boolean getBoolean(boolean defaultValue);

    /**
     * Returns the value as a double.
     * 
     * @param defaultValue
     * @return
     */
    double getDouble(double defaultValue);

    /**
     * Returns the value as an integer.
     * 
     * @param defaultValue
     * @return
     */
    int getInt(int defaultValue);

    /**
     * Returns the value as a long.
     * 
     * @param defaultValue
     * @return
     */
    long getLong(long defaultValue);

    /**
     * Returns the value as a string.
     * 
     * @param defaultValue
     * @return
     */
    String getString(String defaultValue);

    /**
     * Returns the size.
     * 
     * @return
     */
    int size();

    /**
     * Adapts the JSON value to the specified class.
     * 
     * @param adaptClass
     * @return The adapted JSON value value.
     */
    <T> T adapt(Class<T> adaptClass);

    /**
     * 
     * 
     * @param objectClass
     * @return
     */
    <T> T toObject(Class<T> objectClass);

    /**
     * Returns a string representation of the object.
     * 
     * @return A string representation of the object.
     */
    String toString();
}
