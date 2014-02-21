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
import com.google.gson.JsonElement;

import net.signalr.client.json.JsonArray;
import net.signalr.client.json.JsonObject;

/**
 * Represents a GSON based JSON array.
 */
final class GsonArray implements JsonArray {

    /**
     * The GSON instance.
     */
    private final Gson _gson;

    /**
     * The underlying JSON array.
     */
    private final com.google.gson.JsonArray _array;

    /**
     * Initializes a new instance of the {@link GsonArray} class.
     * 
     * @param hson The GSON instance.
     * @param array The underlying JSON array.
     */
    public GsonArray(final Gson gson, final com.google.gson.JsonArray array) {
        if (gson == null) {
            throw new IllegalArgumentException("Gson must not be null");
        }
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }

        _gson = gson;
        _array = array;
    }

    @Override
    public JsonObject getObject(final int index) {
        final JsonElement element = _array.get(index);

        if ((element == null) || element.isJsonObject()) {
            return null;
        }
        final com.google.gson.JsonObject object = element.getAsJsonObject();

        return new GsonObject(_gson, object);
    }

    @Override
    public <T> T toObject(final Class<T> clazz) {
        return _gson.fromJson(_array, clazz);
    }

    @Override
    public String toString() {
        return _array.toString();
    }
}
