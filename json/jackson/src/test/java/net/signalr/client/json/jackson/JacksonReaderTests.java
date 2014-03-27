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
        assertThat(value.getInt(0), is(1));
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
        final JsonValue firstValue;
        final JsonValue secondValue;

        // Act
        reader.readBeginArray();
        firstValue = reader.readValue();
        secondValue = reader.readValue();
        reader.readEndArray();

        // Assert
        assertThat(firstValue.size(), is(0));
        assertThat(firstValue.get("A").getInt(0), is(1));
        assertThat(secondValue.size(), is(0));
        assertThat(secondValue.get("B").getInt(0), is(2));
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
}
