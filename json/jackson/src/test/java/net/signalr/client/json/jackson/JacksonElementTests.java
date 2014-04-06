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
import net.signalr.client.json.DefaultJsonMapper;
import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonMapper;

import org.junit.Before;
import org.junit.Test;

public final class JacksonElementTests {

    private JsonMapper _mapper;

    @Before
    public void setUp() {
        final JsonFactory factory = new JacksonFactory();

        _mapper = new DefaultJsonMapper(factory);
    }

    private JsonElement fromJson(final String text) {
        return _mapper.fromJson(text);
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
        assertNotNull(element);
        assertFalse(element.isArray());
        assertFalse(element.isObject());
        assertFalse(element.isValue());
    }

    @Test
    public void isObjectWithEmptyObjectTest() {
        // Arrange
        final JsonElement element = fromJson("{}");

        // Act
        // Assert
        assertNotNull(element);
        assertFalse(element.isArray());
        assertTrue(element.isObject());
        assertFalse(element.isValue());
    }

    @Test
    public void isArrayWithEmptyArrayTest() {
        // Arrange
        final JsonElement element = fromJson("[]");

        // Act
        // Assert
        assertNotNull(element);
        assertTrue(element.isArray());
        assertFalse(element.isObject());
        assertFalse(element.isValue());
    }

    @Test
    public void isValueWithIntegerValueTest() {
        // Arrange
        final JsonElement element = fromJson("1");

        // Act
        // Assert
        assertNotNull(element);
        assertFalse(element.isArray());
        assertFalse(element.isObject());
        assertTrue(element.isValue());
    }

    @Test
    public void getWithIndexAndArrayTest() {
        // Arrange
        final JsonElement array = fromJson("[[1]]");

        // Act
        final JsonElement element = array.get(0);

        // Assert
        assertNotNull(element);
        assertTrue(element.isArray());
        assertFalse(element.isObject());
        assertFalse(element.isValue());
    }

    @Test
    public void getWithIndexAndObjectTest() {
        // Arrange
        final JsonElement array = fromJson("[{\"A\":1}]");

        // Act
        final JsonElement element = array.get(0);

        // Assert
        assertNotNull(element);
        assertFalse(element.isArray());
        assertTrue(element.isObject());
        assertFalse(element.isValue());
    }

    @Test
    public void getWithIndexAndValueTest() {
        // Arrange
        final JsonElement array = fromJson("[1]");

        // Act
        final JsonElement element = array.get(0);

        // Assert
        assertNotNull(element);
        assertFalse(element.isArray());
        assertFalse(element.isObject());
        assertTrue(element.isValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getWithInvalidIndexTest() {
        // Arrange
        final JsonElement array = fromJson("[1]");

        // Act
        // Assert
        array.get(-1);
    }

    @Test
    public void getWithNameAndArrayTest() {
        // Arrange
        final JsonElement object = fromJson("{\"A\":[1]}");

        // Act
        final JsonElement element = object.get("A");

        // Assert
        assertNotNull(element);
        assertTrue(element.isArray());
        assertFalse(element.isObject());
        assertFalse(element.isValue());
    }

    @Test
    public void getWithNameAndObjectTest() {
        // Arrange
        final JsonElement object = fromJson("{\"A\":{\"A\":1}}");

        // Act
        final JsonElement element = object.get("A");

        // Assert
        assertNotNull(element);
        assertFalse(element.isArray());
        assertTrue(element.isObject());
        assertFalse(element.isValue());
    }

    @Test
    public void getWithNameAndValueTest() {
        // Arrange
        final JsonElement object = fromJson("{\"A\":1}");

        // Act
        final JsonElement element = object.get("A");

        // Assert
        assertNotNull(element);
        assertFalse(element.isArray());
        assertFalse(element.isObject());
        assertTrue(element.isValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWithNullNameTest() {
        // Arrange
        final JsonElement object = fromJson("{\"A\":1}");

        // Act
        // Assert
        object.get(null);
    }

    @Test
    public void getWithEmptyNameTest() {
        // Arrange
        final JsonElement object = fromJson("{\"A\":1}");

        // Act
        final JsonElement element = object.get("");

        // Assert
        assertNotNull(element);
        assertFalse(element.isArray());
        assertFalse(element.isObject());
        assertFalse(element.isValue());
    }

    @Test
    public void getWithUnknownNameTest() {
        // Arrange
        final JsonElement object = fromJson("{\"A\":1}");

        // Act
        final JsonElement element = object.get("B");

        // Assert
        assertNotNull(element);
        assertFalse(element.isArray());
        assertFalse(element.isObject());
        assertFalse(element.isValue());
    }
}
