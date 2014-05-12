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

package net.signalr.client.hub;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.signalr.client.hub.HubResponse;
import net.signalr.client.json.DefaultJsonMapper;
import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonMapper;
import net.signalr.client.json.jackson.JacksonFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class HubResponseTests {

    private JsonMapper _mapper;

    @Before
    public void before() {
        final JsonFactory factory = new JacksonFactory();

        _mapper = new DefaultJsonMapper(factory);
    }

    @Test
    public void deserializeCallbackIdResponseTest() {
        // Arrange
        final String data = "{\"I\":\"1\"}";
        final JsonElement element = _mapper.toElement(data);

        // Act
        final HubResponse response = new HubResponse(element);

        // Assert
        assertNotNull(response);
        assertThat(response.getCallbackId(), is("1"));
    }

    @Test
    public void deserializeMessageResponseTest() {
        // Arrange
        final String data = "{\"C\":\"s-0,298F690\",\"M\":[{\"H\":\"hub\",\"M\":\"update\",\"A\":[{\"Value\":1}]},{\"H\":\"hub\",\"M\":\"update\",\"A\":[{\"Value\":2}]}]}";
        final JsonElement element = _mapper.toElement(data);

        // Act
        final HubResponse response = new HubResponse(element);

        // Assert
        assertNotNull(response);
        assertThat(response.getMessageId(), is("s-0,298F690"));
        final HubMessage[] messages = response.getMessages();

        assertThat(messages.length, is(2));
        assertThat(messages[0].getHubName(), is("hub"));
        assertThat(messages[0].getMethodName(), is("update"));
        assertThat(messages[0].getArguments().toString(), is("[{\"Value\":1}]"));
        assertThat(messages[1].getHubName(), is("hub"));
        assertThat(messages[1].getMethodName(), is("update"));
        assertThat(messages[1].getArguments().toString(), is("[{\"Value\":2}]"));
    }
}
