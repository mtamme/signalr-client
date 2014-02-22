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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.signalr.client.json.JsonArray;
import net.signalr.client.json.JsonObject;

/**
 * Represents a Jackson based JSON array.
 */
final class JacksonArray implements JsonArray {

    /**
     * The object mapper.
     */
    private final ObjectMapper _mapper;

    /**
     * The underlying JSON array.
     */
    private final ArrayNode _array;

    /**
     * Initializes a new instance of the {@link JacksonArray} class.
     * 
     * @param mapper The object mapper.
     * @param array The underlying JSON array.
     */
    public JacksonArray(final ObjectMapper mapper, final ArrayNode array) {
        if (mapper == null) {
            throw new IllegalArgumentException("Mapper must not be null");
        }
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }

        _mapper = mapper;
        _array = array;
    }

    @Override
    public JsonObject getObject(final int index) {
        final JsonNode node = _array.get(index);

        if ((node == null) || !node.isObject()) {
            return null;
        }
        final ObjectNode object = (ObjectNode) node;

        return new JacksonObject(_mapper, object);
    }

    @Override
    public <T> T toObject(final Class<T> clazz) {
        return _mapper.convertValue(_array, clazz);
    }

    @Override
    public String toString() {
        return _array.asText();
    }
}