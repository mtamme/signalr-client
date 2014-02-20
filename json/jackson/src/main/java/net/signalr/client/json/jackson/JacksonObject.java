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

package net.signalr.client.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.signalr.client.json.JsonArray;
import net.signalr.client.json.JsonObject;

/**
 * 
 */
final class JacksonObject implements JsonObject {

    private final ObjectNode _object;

    public JacksonObject(final ObjectNode object) {
        if (object == null) {
            throw new IllegalArgumentException("Object must not be null");
        }

        _object = object;
    }

    @Override
    public boolean getBoolean(final String name, final boolean defaultValue) {
        final JsonNode node = _object.get(name);

        if ((node == null) || !node.isBoolean()) {
            return defaultValue;
        }

        return node.asBoolean();
    }

    @Override
    public int getInt(final String name, final int defaultValue) {
        final JsonNode node = _object.get(name);

        if ((node == null) || !node.isInt()) {
            return defaultValue;
        }

        return node.asInt();
    }

    @Override
    public long getLong(final String name, final long defaultValue) {
        final JsonNode node = _object.get(name);

        if ((node == null) || !node.isLong()) {
            return defaultValue;
        }

        return node.asLong();
    }

    @Override
    public double getDouble(final String name, final double defaultValue) {
        final JsonNode node = _object.get(name);

        if ((node == null) || !node.isDouble()) {
            return defaultValue;
        }

        return node.asDouble();
    }

    @Override
    public String getString(final String name) {
        final JsonNode node = _object.get(name);

        if ((node == null) || !node.isTextual()) {
            return null;
        }

        return node.asText();
    }

    @Override
    public JsonObject getObject(final String name) {
        final JsonNode node = _object.get(name);

        if ((node == null) || !node.isObject()) {
            return null;
        }
        final ObjectNode object = (ObjectNode) node;

        return new JacksonObject(object);
    }

    @Override
    public JsonArray getArray(final String name) {
        final JsonNode node = _object.get(name);

        if ((node == null) || !node.isArray()) {
            return null;
        }
        final ArrayNode array = (ArrayNode) node;

        return new JacksonArray(array);
    }

    @Override
    public <T> T toObject(final Class<T> clazz) {
        return null;
    }

    @Override
    public String toString() {
        return _object.asText();
    }
}
