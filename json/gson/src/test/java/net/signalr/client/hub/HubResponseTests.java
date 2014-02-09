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

import net.signalr.client.hub.HubResponse;
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.json.gson.GsonSerializer;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public final class HubResponseTests {

    private JsonSerializer _serializer;

    @Before
    public void setUp() {
        _serializer = new GsonSerializer();
    }

    @Test
    public void deserializeCallbackIdResponseTest() {
        // Arrange
        final String data = "{\"I\":\"1\"}";

        // Act
        final HubResponse response = _serializer.deserialize(data, HubResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getCallbackId(), is("1"));
    }

    @Test
    public void deserializeMessageResponseTest() {
        // Arrange
        final String data = "{\"C\":\"s-0,298F690\",\"M\":[]}";

        // Act
        final HubResponse response = _serializer.deserialize(data, HubResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getMessageId(), is("s-0,298F690"));
    }
}