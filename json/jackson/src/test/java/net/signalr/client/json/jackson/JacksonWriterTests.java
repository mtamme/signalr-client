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

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonWriter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class JacksonWriterTests {

    private JsonFactory _factory;

    @Before
    public void before() {
        _factory = new JacksonFactory();
    }

    private JsonWriter createWriter(final StringWriter output) {
        return _factory.createWriter(output);
    }

    @Test
    public void writeEmptyObjectTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginObject();
        writer.writeEndObject();
        writer.close();

        // Assert
        assertThat(output.toString(), is("{}"));
    }

    @Test
    public void writeObjectWithNullValueTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginObject();
        writer.writeName("A");
        writer.writeNull();
        writer.writeEndObject();
        writer.close();

        // Assert
        assertThat(output.toString(), is("{\"A\":null}"));
    }

    @Test
    public void writeObjectWithBooleanValueTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginObject();
        writer.writeName("A");
        writer.writeBoolean(true);
        writer.writeEndObject();
        writer.close();

        // Assert
        assertThat(output.toString(), is("{\"A\":true}"));
    }

    @Test
    public void writeObjectWithIntValueTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginObject();
        writer.writeName("A");
        writer.writeInt(1);
        writer.writeEndObject();
        writer.close();

        // Assert
        assertThat(output.toString(), is("{\"A\":1}"));
    }

    @Test
    public void writeObjectWithLongValueTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginObject();
        writer.writeName("A");
        writer.writeLong(1);
        writer.writeEndObject();
        writer.close();

        // Assert
        assertThat(output.toString(), is("{\"A\":1}"));
    }

    @Test
    public void writeObjectWithDoubleValueTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginObject();
        writer.writeName("A");
        writer.writeDouble(1.0);
        writer.writeEndObject();
        writer.close();

        // Assert
        assertThat(output.toString(), is("{\"A\":1.0}"));
    }

    @Test
    public void writeObjectWithStringValueTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginObject();
        writer.writeName("A");
        writer.writeString("1");
        writer.writeEndObject();
        writer.close();

        // Assert
        assertThat(output.toString(), is("{\"A\":\"1\"}"));
    }

    @Test
    public void writeObjectWithIntegerObjectTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginObject();
        writer.writeName("A");
        writer.writeObject(1);
        writer.writeEndObject();
        writer.close();

        // Assert
        assertThat(output.toString(), is("{\"A\":1}"));
    }

    @Test
    public void writeObjectWithMapObjectTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);
        final Map<String, Object> value = new HashMap<String, Object>();

        value.put("A", "1");
        value.put("B", true);

        // Act
        writer.writeBeginObject();
        writer.writeName("A");
        writer.writeObject(value);
        writer.writeEndObject();
        writer.close();

        // Assert
        assertThat(output.toString(), is("{\"A\":{\"A\":\"1\",\"B\":true}}"));
    }

    @Test
    public void writeEmptyArrayTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginArray();
        writer.writeEndArray();
        writer.close();

        // Assert
        assertThat(output.toString(), is("[]"));
    }

    @Test
    public void writeArrayWithNullValueTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginArray();
        writer.writeNull();
        writer.writeEndArray();
        writer.close();

        // Assert
        assertThat(output.toString(), is("[null]"));
    }

    @Test
    public void writeArrayWithBooleanValueTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginArray();
        writer.writeBoolean(true);
        writer.writeEndArray();
        writer.close();

        // Assert
        assertThat(output.toString(), is("[true]"));
    }

    @Test
    public void writeArrayWithIntValueTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginArray();
        writer.writeInt(1);
        writer.writeEndArray();
        writer.close();

        // Assert
        assertThat(output.toString(), is("[1]"));
    }

    @Test
    public void writeArrayWithLongValueTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginArray();
        writer.writeLong(1);
        writer.writeEndArray();
        writer.close();

        // Assert
        assertThat(output.toString(), is("[1]"));
    }

    @Test
    public void writeArrayWithDoubleValueTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginArray();
        writer.writeDouble(1.0);
        writer.writeEndArray();
        writer.close();

        // Assert
        assertThat(output.toString(), is("[1.0]"));
    }

    @Test
    public void writeArrayWithStringValueTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginArray();
        writer.writeString("1");
        writer.writeEndArray();
        writer.close();

        // Assert
        assertThat(output.toString(), is("[\"1\"]"));
    }

    @Test
    public void writeArrayWithIntegerObjectTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);

        // Act
        writer.writeBeginArray();
        writer.writeObject(1);
        writer.writeEndArray();
        writer.close();

        // Assert
        assertThat(output.toString(), is("[1]"));
    }

    @Test
    public void writeArrayWithMapObjectTest() {
        // Arrange
        final StringWriter output = new StringWriter();
        final JsonWriter writer = createWriter(output);
        final Map<String, Object> value = new HashMap<String, Object>();

        value.put("A", "1");
        value.put("B", true);

        // Act
        writer.writeBeginArray();
        writer.writeObject(value);
        writer.writeEndArray();
        writer.close();

        // Assert
        assertThat(output.toString(), is("[{\"A\":\"1\",\"B\":true}]"));
    }
}
