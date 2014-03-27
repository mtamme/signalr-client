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
 * Represents a JSON null element.
 */
public final class JsonNull implements JsonElement {

    /**
     * The singleton instance.
     */
    public static final JsonNull instance = new JsonNull();

    /**
     * Initializes a new instance of the {@link JsonNull} class.
     */
    private JsonNull() {
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
    public Object getUnderlyingElement() {
        return null;
    }

    @Override
    public <T> T toObject(final Class<T> clazz) {
        return null;
    }
}
