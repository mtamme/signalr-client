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

package net.signalr.client.json.gson;

import java.io.StringReader;

import net.signalr.client.json.JsonReader;

import org.junit.Test;

import com.google.gson.Gson;

public final class GsonReaderTests {

    private static JsonReader createReader(final String json) {
        final Gson gson = new Gson();
        final StringReader buffer = new StringReader(json);
        final com.google.gson.stream.JsonReader reader = new com.google.gson.stream.JsonReader(buffer);

        return new GsonReader(gson, reader);
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
    public void readEmptyArrayTest() {
        // Arrange
        final JsonReader reader = createReader("[]");

        // Act
        // Assert
        reader.readBeginArray();
        reader.readEndArray();
    }
}
