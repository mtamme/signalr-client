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

import java.io.StringWriter;

import net.signalr.client.json.JsonSerializer;
import net.signalr.client.json.JsonWriter;

import org.junit.Before;

public final class GsonWriterTest {

    private JsonSerializer _serializer;

    @Before
    public void setUp() {
        _serializer = new GsonSerializer();
    }

    private JsonWriter createWriter(final StringWriter json) {
        return _serializer.createWriter(json);
    }
}
