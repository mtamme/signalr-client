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

package net.signalr.client.hub;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.json.jackson.JacksonSerializer;

import org.junit.Before;
import org.junit.Test;

public final class HubRequestTests {

    private JsonSerializer _serializer;

    @Before
    public void setUp() {
        _serializer = new JacksonSerializer();
    }

    @Test
    public void deserializeTest() {
        // Arrange
        final HubRequest request = new HubRequest();

        request.setCallbackId("1");

        // Act
        final String data = _serializer.toJson(request);

        // Assert
        assertNotNull(data);
        assertThat(data, is("{\"I\":\"1\"}"));
    }
}