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

package net.signalr.client.json.gson;

import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonEmpty;
import net.signalr.client.json.JsonException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }

        final JsonArray array = (JsonArray) _element;
        // Throws: IndexOutOfBoundsException - if index is negative or
        // greater than or equal to the size() of the array.
        final com.google.gson.JsonElement element = array.get(index);

        return new GsonElement(_gson, element);
    }

    @Override
    public JsonElement get(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }

        if (!_element.isJsonObject()) {
            return JsonEmpty.INSTANCE;
        }

        final JsonObject object = (JsonObject) _element;
        // Returns: The member matching the name. Null if no such member exists.
        final com.google.gson.JsonElement element = object.get(name);

        if (element == null) {
            return JsonEmpty.INSTANCE;
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
    public <T> T toObject(final Class<T> type, final T defaultValue) {
        if (type == null) {
            throw new IllegalArgumentException("Type must not be null");
        }

        if (_element.isJsonNull()) {
            return null;
        }
        if (!_element.isJsonArray() && !_element.isJsonObject()) {
            return defaultValue;
        }
        try {
            return _gson.fromJson(_element, type);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public int hashCode() {
        return _element.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof GsonElement)) {
            return false;
        }

        final GsonElement element = (GsonElement) other;

        return _element.equals(element._element);
    }

    @Override
    public String toString() {
        return _element.toString();
    }
}
