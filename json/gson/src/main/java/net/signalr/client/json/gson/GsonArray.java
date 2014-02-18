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
final class GsonArray implements JsonArray {

    private final com.google.gson.JsonArray _array;

    public GsonArray(final com.google.gson.JsonArray array) {
        _array = array;
    }

    @Override
    public JsonObject getObject(final int index) {
        final JsonElement element = _array.get(index);

        if (element == null) {
            return null;
        }
        final com.google.gson.JsonObject object = element.getAsJsonObject();

        return new GsonObject(object);
    }

    @Override
    public <T> T toObject(final Class<T> clazz) {
        return null;
    }

    @Override
    public String toString() {
        return _array.toString();
    }
}
