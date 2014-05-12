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

package net.signalr.client.json.jackson;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonReader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class JacksonReaderTests {

    private JsonFactory _factory;

    @Before
    public void before() {
        _factory = new JacksonFactory();
    }

    private JsonReader createReader(final String text) {
        final StringReader input = new StringReader(text);

        return _factory.createReader(input);
    }

    @Test
    public void readEmptyObjectTest() {
        // Arrange
        final JsonReader reader = createReader("{}");

        // Act
        // Assert
        reader.readBeginObject();
        reader.readEndObject();
    }

    @Test
    public void readObjectWithNullValueTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":null}");

        // Act
        // Assert
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                reader.readNull();
            }
        }
        reader.readEndObject();
    }

    @Test
    public void readObjectWithBooleanValueTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":true}");
        Boolean value = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                value = reader.readBoolean();
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(value);
        assertThat(value, is(true));
    }

    @Test
    public void readObjectWithIntValueTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":1}");
        Integer value = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                value = reader.readInt();
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(value);
        assertThat(value, is(1));
    }

    @Test
    public void readObjectWithLongValueTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":1}");
        Long value = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                value = reader.readLong();
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(value);
        assertThat(value, is(1L));
    }

    @Test
    public void readObjectWithDoubleValueTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":1.0}");
        Double value = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                value = reader.readDouble();
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(value);
        assertThat(value, is(1.0));
    }

    @Test
    public void readObjectWithStringValueTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":\"1\"}");
        String value = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                value = reader.readString();
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(value);
        assertThat(value, is("1"));
    }

    @Test
    public void readObjectWithIntegerElementTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":1}");
        JsonElement element = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                element = reader.readElement();
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(element);
        assertTrue(element.isValue());
        assertThat(element.getInt(0), is(1));
    }

    @Test
    public void readObjectWithObjectElementsTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":{\"A\":1},\"B\":{\"B\":2}}");
        final Map<String, JsonElement> elements = new HashMap<String, JsonElement>();

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();
            final JsonElement element = reader.readElement();

            elements.put(name, element);
        }
        reader.readEndObject();

        // Assert
        assertThat(elements.size(), is(2));
        assertTrue(elements.get("A").isObject());
        assertThat(elements.get("A").size(), is(0));
        assertThat(elements.get("A").get("A").getInt(0), is(1));
        assertTrue(elements.get("B").isObject());
        assertThat(elements.get("B").size(), is(0));
        assertThat(elements.get("B").get("B").getInt(0), is(2));
    }

    @Test
    public void readObjectWithArrayElementTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":[1,2]}");
        JsonElement element = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                element = reader.readElement();
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(element);
        assertTrue(element.isArray());
        assertThat(element.size(), is(2));
        assertThat(element.get(0).getInt(0), is(1));
    }

    @Test
    public void readObjectWithIntegerObjectTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":1}");
        Integer object = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                object = reader.readObject(Integer.class);
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(object);
        assertThat(object, is(1));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void readObjectWithMapObjectTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":{\"A\":\"1\",\"B\":true}}");
        Map<String, Object> object = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                object = reader.readObject(Map.class);
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(object);
        assertThat(object.size(), is(2));
        assertThat(object.get("A"), is((Object) "1"));
        assertThat(object.get("B"), is((Object) true));
    }

    @Test
    public void readEmptyArrayTest() {
        // Arrange
        final JsonReader reader = createReader("[]");

        // Act
        // Assert
        reader.readBeginArray();
        reader.readEndArray();
    }

    @Test
    public void readArrayWithNullValueTest() {
        // Arrange
        final JsonReader reader = createReader("[null]");

        // Act
        // Assert
        reader.readBeginArray();
        reader.readNull();
        reader.readEndArray();
    }

    @Test
    public void readArrayWithBooleanValueTest() {
        // Arrange
        final JsonReader reader = createReader("[true]");
        final boolean value;

        // Act
        reader.readBeginArray();
        value = reader.readBoolean();
        reader.readEndArray();

        // Assert
        assertThat(value, is(true));
    }

    @Test
    public void readArrayWithIntValueTest() {
        // Arrange
        final JsonReader reader = createReader("[1]");
        final int value;

        // Act
        reader.readBeginArray();
        value = reader.readInt();
        reader.readEndArray();

        // Assert
        assertThat(value, is(1));
    }

    @Test
    public void readArrayWithLongValueTest() {
        // Arrange
        final JsonReader reader = createReader("[1]");
        final long value;

        // Act
        reader.readBeginArray();
        value = reader.readLong();
        reader.readEndArray();

        // Assert
        assertThat(value, is(1L));
    }

    @Test
    public void readArrayWithDoubleValueTest() {
        // Arrange
        final JsonReader reader = createReader("[1.0]");
        final double value;

        // Act
        reader.readBeginArray();
        value = reader.readDouble();
        reader.readEndArray();

        // Assert
        assertThat(value, is(1.0));
    }

    @Test
    public void readArrayWithStringValueTest() {
        // Arrange
        final JsonReader reader = createReader("[\"1\"]");
        final String value;

        // Act
        reader.readBeginArray();
        value = reader.readString();
        reader.readEndArray();

        // Assert
        assertThat(value, is("1"));
    }

    @Test
    public void readArrayWithIntegerElementTest() {
        // Arrange
        final JsonReader reader = createReader("[1]");
        final JsonElement element;

        // Act
        reader.readBeginArray();
        element = reader.readElement();
        reader.readEndArray();

        // Assert
        assertTrue(element.isValue());
        assertThat(element.getInt(0), is(1));
    }

    @Test
    public void readArrayWithObjectElementsTest() {
        // Arrange
        final JsonReader reader = createReader("[{\"A\":1},{\"B\":2}]");
        final List<JsonElement> elements = new ArrayList<JsonElement>();

        // Act
        reader.readBeginArray();
        while (reader.read()) {
            final JsonElement element = reader.readElement();

            elements.add(element);
        }
        reader.readEndArray();

        // Assert
        assertThat(elements.size(), is(2));
        assertTrue(elements.get(0).isObject());
        assertThat(elements.get(0).size(), is(0));
        assertThat(elements.get(0).get("A").getInt(0), is(1));
        assertTrue(elements.get(1).isObject());
        assertThat(elements.get(1).size(), is(0));
        assertThat(elements.get(1).get("B").getInt(0), is(2));
    }

    @Test
    public void readArrayWithArrayElementTest() {
        // Arrange
        final JsonReader reader = createReader("[[1,2]]");
        final JsonElement element;

        // Act
        reader.readBeginArray();
        element = reader.readElement();
        reader.readEndArray();

        // Assert
        assertTrue(element.isArray());
        assertThat(element.size(), is(2));
        assertThat(element.get(0).getInt(0), is(1));
    }

    @Test
    public void readArrayWithIntegerObjectTest() {
        // Arrange
        final JsonReader reader = createReader("[1]");
        final Integer object;

        // Act
        reader.readBeginArray();
        object = reader.readObject(Integer.class);
        reader.readEndArray();

        // Assert
        assertThat(object, is(1));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void readArrayWithMapObjectTest() {
        // Arrange
        final JsonReader reader = createReader("[{\"A\":\"1\",\"B\":true}]");
        final Map<String, Object> object;

        // Act
        reader.readBeginArray();
        object = reader.readObject(Map.class);
        reader.readEndArray();

        // Assert
        assertThat(object.size(), is(2));
        assertThat(object.get("A"), is((Object) "1"));
        assertThat(object.get("B"), is((Object) true));
    }
}
