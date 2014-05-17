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

package net.signalr.client.transport;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.signalr.client.json.DefaultJsonMapper;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonMapper;
import net.signalr.client.json.gson.GsonFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class PingResponseTest {

    private JsonMapper _mapper;

    @Before
    public void before() {
        final JsonFactory factory = new GsonFactory();

        _mapper = new DefaultJsonMapper(factory);
    }

    @Test
    public void deserializeTest() {
        // Arrange
        final String data = "{\"Response\":\"pong\"}";

        // Act
        final PingResponse response = _mapper.toObject(data, PingResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getValue(), is("pong"));
    }
}
