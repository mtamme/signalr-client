/*
 * Copyright 2014 Martin Tamme
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

package net.signalr.client.json.jackson;

import static org.junit.Assert.*;
import net.signalr.client.json.DefaultJsonMapper;
import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class JacksonElementTests {

    private JsonMapper _mapper;

    @Before
    public void before() {
        final JsonFactory factory = new JacksonFactory();

        _mapper = new DefaultJsonMapper(factory);
    }

    private JsonElement toElement(final String text) {
        return _mapper.toElement(text);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTextTest() {
        // Arrange
        // Act
        // Assert
        toElement(null);
    }

    @Test
    public void emptyTextTest() {
        // Arrange
        final JsonElement element = toElement("");

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
        final JsonElement element = toElement("{}");

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
        final JsonElement element = toElement("[]");

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
        final JsonElement element = toElement("1");

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
        final JsonElement array = toElement("[[1]]");

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
        final JsonElement array = toElement("[{\"A\":1}]");

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
        final JsonElement array = toElement("[1]");

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
        final JsonElement array = toElement("[1]");

        // Act
        // Assert
        array.get(-1);
    }

    @Test
    public void getWithNameAndArrayTest() {
        // Arrange
        final JsonElement object = toElement("{\"A\":[1]}");

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
        final JsonElement object = toElement("{\"A\":{\"A\":1}}");

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
        final JsonElement object = toElement("{\"A\":1}");

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
        final JsonElement object = toElement("{\"A\":1}");

        // Act
        // Assert
        object.get(null);
    }

    @Test
    public void getWithEmptyNameTest() {
        // Arrange
        final JsonElement object = toElement("{\"A\":1}");

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
        final JsonElement object = toElement("{\"A\":1}");

        // Act
        final JsonElement element = object.get("B");

        // Assert
        assertNotNull(element);
        assertFalse(element.isArray());
        assertFalse(element.isObject());
        assertFalse(element.isValue());
    }
}
