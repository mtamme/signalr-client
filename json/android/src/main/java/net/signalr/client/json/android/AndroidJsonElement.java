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

package net.signalr.client.json.android;

import net.signalr.client.json.JsonElement;

/**
 * Represents an Android JSON element.
 */
final class AndroidJsonElement implements JsonElement {

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
        return null;
    }

    @Override
    public JsonElement get(final String name) {
        return null;
    }

    @Override
    public boolean getBoolean(final boolean defaultValue) {
        return false;
    }

    @Override
    public double getDouble(final double defaultValue) {
        return 0;
    }

    @Override
    public int getInt(final int defaultValue) {
        return 0;
    }

    @Override
    public long getLong(final long defaultValue) {
        return 0;
    }

    @Override
    public String getString(final String defaultValue) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public <T> T unwrap(final Class<T> type) {
        return null;
    }

    @Override
    public <T> T toObject(final Class<T> type) {
        return null;
    }
}
