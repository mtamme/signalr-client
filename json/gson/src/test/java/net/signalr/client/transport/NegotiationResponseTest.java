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
public final class NegotiationResponseTest {

    private JsonMapper _mapper;

    @Before
    public void before() {
        final JsonFactory factory = new GsonFactory();

        _mapper = new DefaultJsonMapper(factory);
    }

    @Test
    public void deserializeTest() {
        // Arrange
        final String data = "{\"Url\":\"/signalr\",\"ConnectionToken\":\"Z1IuK7USZw4BwrDFbF8S+Hec4Mitkwe0+3N/FEZoQD8yVObSvtFdUfUJhKprVhjfXYlu1OLv2em/zMGN5ZK0Vr5H6ZqTvXaH+7Y8ee7yGjATfvZB\",\"ConnectionId\":\"0fa8593e-448e-4c98-9b21-2d95dce3adcc\",\"KeepAliveTimeout\":80.0,\"DisconnectTimeout\":120.0,\"TryWebSockets\":true,\"ProtocolVersion\":\"1.3\",\"TransportConnectTimeout\":5.0}";

        // Act
        final NegotiationResponse response = _mapper.toObject(data, NegotiationResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getRelativeUrl(), is("/signalr"));
        assertThat(response.getConnectionToken(), is("Z1IuK7USZw4BwrDFbF8S+Hec4Mitkwe0+3N/FEZoQD8yVObSvtFdUfUJhKprVhjfXYlu1OLv2em/zMGN5ZK0Vr5H6ZqTvXaH+7Y8ee7yGjATfvZB"));
        assertThat(response.getConnectionId(), is("0fa8593e-448e-4c98-9b21-2d95dce3adcc"));
        assertThat(response.getKeepAliveTimeout(), is(80000L));
        assertThat(response.getDisconnectTimeout(), is(120000L));
        assertThat(response.getTryWebSockets(), is(true));
        assertThat(response.getProtocolVersion(), is("1.3"));
        assertThat(response.getConnectTimeout(), is(5000L));
    }

    @Test
    public void deserializeWithoutKeepAliveTimeoutTest() {
        // Arrange
        final String data = "{\"Url\":\"/signalr\",\"ConnectionToken\":\"Z1IuK7USZw4BwrDFbF8S+Hec4Mitkwe0+3N/FEZoQD8yVObSvtFdUfUJhKprVhjfXYlu1OLv2em/zMGN5ZK0Vr5H6ZqTvXaH+7Y8ee7yGjATfvZB\",\"ConnectionId\":\"0fa8593e-448e-4c98-9b21-2d95dce3adcc\",\"DisconnectTimeout\":120.0,\"TryWebSockets\":true,\"ProtocolVersion\":\"1.3\",\"TransportConnectTimeout\":5.0}";

        // Act
        final NegotiationResponse response = _mapper.toObject(data, NegotiationResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getRelativeUrl(), is("/signalr"));
        assertThat(response.getConnectionToken(), is("Z1IuK7USZw4BwrDFbF8S+Hec4Mitkwe0+3N/FEZoQD8yVObSvtFdUfUJhKprVhjfXYlu1OLv2em/zMGN5ZK0Vr5H6ZqTvXaH+7Y8ee7yGjATfvZB"));
        assertThat(response.getConnectionId(), is("0fa8593e-448e-4c98-9b21-2d95dce3adcc"));
        assertThat(response.getKeepAliveTimeout(), is(-1L));
        assertThat(response.getDisconnectTimeout(), is(120000L));
        assertThat(response.getTryWebSockets(), is(true));
        assertThat(response.getProtocolVersion(), is("1.3"));
        assertThat(response.getConnectTimeout(), is(5000L));
    }
}
