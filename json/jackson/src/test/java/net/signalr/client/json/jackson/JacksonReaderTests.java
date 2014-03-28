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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.signalr.client.json.JsonException;
import net.signalr.client.json.JsonReader;
import net.signalr.client.json.JsonValue;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JacksonReaderTests {

    private static JsonReader createReader(final String json) {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonFactory factory = new JsonFactory();
        JsonParser parser;
        try {
            parser = factory.createParser(json);
        } catch (final Exception e) {
            throw new JsonException(e);
        }

        return new JacksonReader(mapper, parser);
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
    public void readObjectWithValueTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":1}");
        JsonValue value = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                value = reader.readValue();
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(value);
        assertThat(value.getInt(0), is(1));
    }

    @Test
    public void readObjectWithPrimitiveValueTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":1}");
        JsonValue value = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                value = reader.readValue();
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(value);
        assertThat(value.getInt(0), is(1));
    }

    @Test
    public void readObjectWithObjectValueTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":{\"A\":1},\"B\":{\"B\":2}}");
        final Map<String, JsonValue> values = new HashMap<String, JsonValue>();

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();
            final JsonValue value = reader.readValue();

            values.put(name, value);
        }
        reader.readEndObject();

        // Assert
        assertThat(values.size(), is(2));
        assertThat(values.get("A").size(), is(0));
        assertThat(values.get("A").get("A").getInt(0), is(1));
        assertThat(values.get("B").size(), is(0));
        assertThat(values.get("B").get("B").getInt(0), is(2));
    }

    @Test
    public void readObjectWithArrayValueTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":[1,2]}");
        JsonValue value = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                value = reader.readValue();
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(value);
        assertThat(value.size(), is(2));
        assertThat(value.get(0).getInt(0), is(1));
    }

    @Test
    public void readObjectWithIntegerObjectTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":1}");
        Integer value = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                value = reader.readObject(Integer.class);
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(value);
        assertThat(value, is(1));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void readObjectWithMapObjectTest() {
        // Arrange
        final JsonReader reader = createReader("{\"A\":{\"A\":\"1\",\"B\":true}}");
        Map<String, Object> value = null;

        // Act
        reader.readBeginObject();
        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("A")) {
                value = reader.readObject(Map.class);
            }
        }
        reader.readEndObject();

        // Assert
        assertNotNull(value);
        assertThat(value.size(), is(2));
        assertThat(value.get("A"), is((Object) "1"));
        assertThat(value.get("B"), is((Object) true));
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
    public void readArrayWithPrimitiveValueTest() {
        // Arrange
        final JsonReader reader = createReader("[1]");
        final JsonValue value;

        // Act
        reader.readBeginArray();
        value = reader.readValue();
        reader.readEndArray();

        // Assert
        assertThat(value.getInt(0), is(1));
    }

    @Test
    public void readArrayWithObjectValueTest() {
        // Arrange
        final JsonReader reader = createReader("[{\"A\":1},{\"B\":2}]");
        final List<JsonValue> values = new ArrayList<JsonValue>();

        // Act
        reader.readBeginArray();
        while (reader.read()) {
            final JsonValue value = reader.readValue();

            values.add(value);
        }
        reader.readEndArray();

        // Assert
        assertThat(values.size(), is(2));
        assertThat(values.get(0).size(), is(0));
        assertThat(values.get(0).get("A").getInt(0), is(1));
        assertThat(values.get(1).size(), is(0));
        assertThat(values.get(1).get("B").getInt(0), is(2));
    }

    @Test
    public void readArrayWithArrayValueTest() {
        // Arrange
        final JsonReader reader = createReader("[[1,2]]");
        final JsonValue value;

        // Act
        reader.readBeginArray();
        value = reader.readValue();
        reader.readEndArray();

        // Assert
        assertThat(value.size(), is(2));
        assertThat(value.get(0).getInt(0), is(1));
    }

    @Test
    public void readArrayWithIntegerObjectTest() {
        // Arrange
        final JsonReader reader = createReader("[1]");
        final Integer value;

        // Act
        reader.readBeginArray();
        value = reader.readObject(Integer.class);
        reader.readEndArray();

        // Assert
        assertThat(value, is(1));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void readArrayWithMapObjectTest() {
        // Arrange
        final JsonReader reader = createReader("[{\"A\":\"1\",\"B\":true}]");
        final Map<String, Object> value;

        // Act
        reader.readBeginArray();
        value = reader.readObject(Map.class);
        reader.readEndArray();

        // Assert
        assertThat(value.size(), is(2));
        assertThat(value.get("A"), is((Object) "1"));
        assertThat(value.get("B"), is((Object) true));
    }
}
