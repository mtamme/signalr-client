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

import static org.junit.Assert.*;

import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonSerializer;

import org.junit.Before;
import org.junit.Test;

public final class JacksonElementTests {

    private JsonSerializer _serializer;

    @Before
    public void setUp() {
        _serializer = new JacksonSerializer();
    }

    private JsonElement fromJson(final String text) {
        return _serializer.fromJson(text);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTextTest() {
        // Arrange
        // Act
        // Assert
        fromJson(null);
    }

    @Test
    public void emptyTextTest() {
        // Arrange
        final JsonElement element = fromJson("");

        // Act
        // Assert
        assertFalse(element.isArray());
        assertFalse(element.isObject());
        assertFalse(element.isValue());
    }

    @Test
    public void emptyObjectTest() {
        // Arrange
        final JsonElement element = fromJson("{}");

        // Act
        // Assert
        assertFalse(element.isArray());
        assertTrue(element.isObject());
        assertFalse(element.isValue());
    }

    @Test
    public void emptyArrayTest() {
        // Arrange
        final JsonElement element = fromJson("[]");

        // Act
        // Assert
        assertTrue(element.isArray());
        assertFalse(element.isObject());
        assertFalse(element.isValue());
    }

    @Test
    public void integerValueTest() {
        // Arrange
        final JsonElement element = fromJson("1");

        // Act
        // Assert
        assertFalse(element.isArray());
        assertFalse(element.isObject());
        assertTrue(element.isValue());
    }
}
