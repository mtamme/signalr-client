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

package net.signalr.client.json.gson;

import com.google.gson.JsonElement;

import net.signalr.client.json.JsonArray;
import net.signalr.client.json.JsonObject;

/**
 * 
 */
final class GsonObject implements JsonObject {

    private final com.google.gson.JsonObject _object;

    public GsonObject(com.google.gson.JsonObject object) {
        if (object == null) {
            throw new IllegalArgumentException("Object must not be null");
        }

        _object = object;
    }

    @Override
    public boolean getBoolean(final String name, final boolean defaultValue) {
        final JsonElement element = _object.get(name);

        if ((element == null) || element.isJsonPrimitive()) {
            return defaultValue;
        }

        return element.getAsBoolean();
    }

    @Override
    public int getInt(final String name, final int defaultValue) {
        final JsonElement element = _object.get(name);

        if ((element == null) || element.isJsonPrimitive()) {
            return defaultValue;
        }

        return element.getAsInt();
    }

    @Override
    public long getLong(final String name, final long defaultValue) {
        final JsonElement element = _object.get(name);

        if ((element == null) || element.isJsonPrimitive()) {
            return defaultValue;
        }

        return element.getAsLong();
    }

    @Override
    public double getDouble(final String name, final double defaultValue) {
        final JsonElement element = _object.get(name);

        if ((element == null) || element.isJsonPrimitive()) {
            return defaultValue;
        }

        return element.getAsDouble();
    }

    @Override
    public String getString(final String name) {
        final JsonElement element = _object.get(name);

        if ((element == null) || element.isJsonPrimitive()) {
            return null;
        }

        return element.getAsString();
    }

    @Override
    public JsonObject getObject(final String name) {
        final JsonElement element = _object.get(name);

        if ((element == null) || element.isJsonObject()) {
            return null;
        }
        final com.google.gson.JsonObject object = element.getAsJsonObject();

        return new GsonObject(object);
    }

    @Override
    public JsonArray getArray(final String name) {
        final JsonElement element = _object.get(name);

        if ((element == null) || element.isJsonArray()) {
            return null;
        }
        final com.google.gson.JsonArray array = element.getAsJsonArray();

        return new GsonArray(array);
    }

    @Override
    public <T> T toObject(final Class<T> clazz) {
        return null;
    }

    @Override
    public String toString() {
        return _object.toString();
    }
}
