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
 * Represents an empty JSON element.
 */
public final class JsonEmpty implements JsonElement {

    /**
     * The singleton instance.
     */
    public static final JsonEmpty INSTANCE = new JsonEmpty();

    /**
     * Initializes a new instance of the {@link JsonEmpty} class.
     */
    private JsonEmpty() {
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public boolean isValue() {
        return false;
    }

    @Override
    public JsonElement get(final int index) {
        return this;
    }

    @Override
    public JsonElement get(final String name) {
        return this;
    }

    @Override
    public boolean getBoolean(final boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public double getDouble(final double defaultValue) {
        return defaultValue;
    }

    @Override
    public int getInt(final int defaultValue) {
        return defaultValue;
    }

    @Override
    public long getLong(final long defaultValue) {
        return defaultValue;
    }

    @Override
    public String getString(final String defaultValue) {
        return defaultValue;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public <T> T unwrap(final Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T toObject(final Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        return JsonEmpty.class.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (other instanceof JsonEmpty);
    }

    @Override
    public String toString() {
        return "";
    }
}
