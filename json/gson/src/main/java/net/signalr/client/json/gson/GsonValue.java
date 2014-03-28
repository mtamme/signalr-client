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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonValue;

/**
 * Represents a GSON based JSON value.
 */
final class GsonValue implements JsonValue {

    /**
     * The GSON instance.
     */
    private final Gson _gson;

    /**
     * The underlying JSON element.
     */
    private final JsonElement _element;

    /**
     * Initializes a new instance of the {@link GsonValue} class.
     * 
     * @param gson The GSON instance.
     * @param element The underlying JSON element.
     */
    public GsonValue(final Gson gson, final JsonElement element) {
        if (gson == null) {
            throw new IllegalArgumentException("Gson must not be null");
        }
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null");
        }

        _gson = gson;
        _element = element;
    }

    @Override
    public JsonValue get(final int index) {
        if (!_element.isJsonArray()) {
            return JsonValue.NONE;
        }
        final JsonArray array = (JsonArray) _element;
        final JsonElement element = array.get(index);

        if (element == null) {
            return JsonValue.NONE;
        }

        return new GsonValue(_gson, element);
    }

    @Override
    public JsonValue get(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }

        if (!_element.isJsonObject()) {
            return JsonValue.NONE;
        }
        final JsonObject object = (JsonObject) _element;
        final JsonElement element = object.get(name);

        if (element == null) {
            return JsonValue.NONE;
        }

        return new GsonValue(_gson, element);
    }

    @Override
    public boolean getBoolean(final boolean defaultValue) {
        try {
            return _element.getAsBoolean();
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    @Override
    public double getDouble(final double defaultValue) {
        try {
            return _element.getAsDouble();
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    @Override
    public int getInt(final int defaultValue) {
        try {
            return _element.getAsInt();
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    @Override
    public long getLong(final long defaultValue) {
        try {
            return _element.getAsLong();
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    @Override
    public String getString(final String defaultValue) {
        try {
            return _element.getAsString();
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    @Override
    public <T> T adapt(final Class<T> adaptClass) {
        if (adaptClass == null) {
            throw new IllegalArgumentException("Adapt class must not be null");
        }

        return adaptClass.cast(_element);
    }

    @Override
    public int size() {
        if (!_element.isJsonArray()) {
            return 0;
        }

        final JsonArray array = (JsonArray) _element;

        return array.size();
    }

    @Override
    public <T> T toObject(final Class<T> objectClass) {
        if (objectClass == null) {
            throw new IllegalArgumentException("Object class must not be null");
        }

        try {
            return _gson.fromJson(_element, objectClass);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public String toString() {
        return _element.toString();
    }
}
