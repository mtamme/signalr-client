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

package net.signalr.client.transport;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.signalr.client.json.DefaultJsonMapper;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonMapper;
import net.signalr.client.json.gson.GsonFactory;

import org.junit.Before;
import org.junit.Test;

public final class PingResponseTests {

    private JsonMapper _mapper;

    @Before
    public void setUp() {
        final JsonFactory factory = new GsonFactory();

        _mapper = new DefaultJsonMapper(factory);
    }

    @Test
    public void deserializeTest() {
        // Arrange
        final String data = "{\"Response\":\"pong\"}";

        // Act
        final PingResponse response = _mapper.fromJson(data, PingResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getValue(), is("pong"));
    }
}
