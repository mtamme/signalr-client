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

import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonEmpty;
import net.signalr.client.json.JsonException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a Jackson based JSON element.
 */
public class JacksonElement implements JsonElement {

    /**
     * The object mapper.
     */
    private final ObjectMapper _mapper;

    /**
     * The underlying node.
     */
    private final JsonNode _node;

    /**
     * Initializes a new instance of the {@link JacksonElement} class.
     * 
     * @param mapper The object mapper.
     * @param node The underlying node.
     */
    public JacksonElement(final ObjectMapper mapper, final JsonNode node) {
        if (mapper == null) {
            throw new IllegalArgumentException("Mapper must not be null");
        }
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null");
        }

        _mapper = mapper;
        _node = node;
    }

    @Override
    public boolean isArray() {
        return _node.isArray();
    }

    @Override
    public boolean isObject() {
        return _node.isObject();
    }

    @Override
    public boolean isValue() {
        return !_node.isArray() && !_node.isObject();
    }

    @Override
    public JsonElement get(final int index) {
        if (!_node.isArray()) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }

        // Returns: Node that represent value of the specified element,
        // if this node is an array and has specified element.
        // Null otherwise.
        final JsonNode node = _node.get(index);

        if (node == null) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }

        return new JacksonElement(_mapper, node);
    }

    @Override
    public JsonElement get(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }

        if (!_node.isObject()) {
            return JsonEmpty.INSTANCE;
        }

        // Returns: Node that represent value of the specified field,
        // if this node is an object and has value for the specified field.
        // Null otherwise.
        final JsonNode node = _node.get(name);

        if (node == null) {
            return JsonEmpty.INSTANCE;
        }

        return new JacksonElement(_mapper, node);
    }

    @Override
    public boolean getBoolean(final boolean defaultValue) {
        if (!_node.isBoolean()) {
            return defaultValue;
        }

        return _node.asBoolean();
    }

    @Override
    public double getDouble(final double defaultValue) {
        if (!_node.isDouble()) {
            return defaultValue;
        }

        return _node.asDouble();
    }

    @Override
    public int getInt(final int defaultValue) {
        if (!_node.isInt()) {
            return defaultValue;
        }

        return _node.asInt();
    }

    @Override
    public long getLong(final long defaultValue) {
        if (!_node.isLong()) {
            return defaultValue;
        }

        return _node.asLong();
    }

    @Override
    public String getString(final String defaultValue) {
        if (!_node.isTextual()) {
            return defaultValue;
        }

        return _node.asText();
    }

    @Override
    public <T> T unwrap(final Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type must not be null");
        }

        return type.cast(_node);
    }

    @Override
    public int size() {
        if (!_node.isArray()) {
            return 0;
        }

        return _node.size();
    }

    @Override
    public <T> T toObject(final Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type must not be null");
        }

        try {
            return _mapper.convertValue(_node, type);
        } catch (final Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public int hashCode() {
        return _node.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof JacksonElement)) {
            return false;
        }

        final JacksonElement element = (JacksonElement) other;

        return _node.equals(element._node);
    }

    @Override
    public String toString() {
        return _node.toString();
    }
}
