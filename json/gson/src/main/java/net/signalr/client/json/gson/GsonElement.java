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
import com.google.gson.JsonObject;

import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonElement;

/**
 * Represents a GSON based JSON element.
 */
final class GsonElement implements JsonElement {

    /**
     * The GSON instance.
     */
    private final Gson _gson;

    /**
     * The underlying element.
     */
    private final com.google.gson.JsonElement _element;

    /**
     * Initializes a new instance of the {@link GsonElement} class.
     * 
     * @param gson The GSON instance.
     * @param element The underlying JSON element.
     */
    public GsonElement(final Gson gson, final com.google.gson.JsonElement element) {
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
    public boolean isArray() {
        return _element.isJsonArray();
    }

    @Override
    public boolean isObject() {
        return _element.isJsonObject();
    }

    @Override
    public boolean isValue() {
        return _element.isJsonPrimitive() || _element.isJsonNull();
    }

    @Override
    public JsonElement get(final int index) {
        if (!_element.isJsonArray()) {
            return JsonElement.NONE;
        }

        final JsonArray array = (JsonArray) _element;
        final com.google.gson.JsonElement element;

        try {
            element = array.get(index);
        } catch (final Exception e) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }

        return new GsonElement(_gson, element);
    }

    @Override
    public JsonElement get(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        if (!_element.isJsonObject()) {
            return JsonElement.NONE;
        }

        final JsonObject object = (JsonObject) _element;
        final com.google.gson.JsonElement element = object.get(name);

        if (element == null) {
            return JsonElement.NONE;
        }

        return new GsonElement(_gson, element);
    }

    @Override
    public boolean getBoolean(final boolean defaultValue) {
        if (!_element.isJsonPrimitive()) {
            return defaultValue;
        }

        try {
            return _element.getAsBoolean();
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    @Override
    public double getDouble(final double defaultValue) {
        if (!_element.isJsonPrimitive()) {
            return defaultValue;
        }

        try {
            return _element.getAsDouble();
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    @Override
    public int getInt(final int defaultValue) {
        if (!_element.isJsonPrimitive()) {
            return defaultValue;
        }

        try {
            return _element.getAsInt();
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    @Override
    public long getLong(final long defaultValue) {
        if (!_element.isJsonPrimitive()) {
            return defaultValue;
        }

        try {
            return _element.getAsLong();
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    @Override
    public String getString(final String defaultValue) {
        if (!_element.isJsonPrimitive()) {
            return defaultValue;
        }

        try {
            return _element.getAsString();
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    @Override
    public <T> T unwrap(final Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type must not be null");
        }

        return type.cast(_element);
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
    public <T> T toObject(final Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type must not be null");
        }

        try {
            return _gson.fromJson(_element, type);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public String toString() {
        return _element.toString();
    }
}
