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

import net.signalr.client.json.JsonSerializer;
import net.signalr.client.json.gson.GsonSerializer;
import net.signalr.client.transport.NegotiationResponse;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public final class NegotiationResponseTests {

    private JsonSerializer _serializer;

    @Before
    public void setUp() {
        _serializer = new GsonSerializer();
    }

    @Test
    public void deserializeTest() {
        // Arrange
        final String data = "{\"Url\":\"/signalr\",\"ConnectionToken\":\"Z1IuK7USZw4BwrDFbF8S+Hec4Mitkwe0+3N/FEZoQD8yVObSvtFdUfUJhKprVhjfXYlu1OLv2em/zMGN5ZK0Vr5H6ZqTvXaH+7Y8ee7yGjATfvZB\",\"ConnectionId\":\"0fa8593e-448e-4c98-9b21-2d95dce3adcc\",\"KeepAliveTimeout\":80.0,\"DisconnectTimeout\":120.0,\"TryWebSockets\":true,\"ProtocolVersion\":\"1.3\",\"TransportConnectTimeout\":5.0}";

        // Act
        final NegotiationResponse response = _serializer.fromJson(data, NegotiationResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getUrl(), is("/signalr"));
        assertThat(response.getConnectionToken(), is("Z1IuK7USZw4BwrDFbF8S+Hec4Mitkwe0+3N/FEZoQD8yVObSvtFdUfUJhKprVhjfXYlu1OLv2em/zMGN5ZK0Vr5H6ZqTvXaH+7Y8ee7yGjATfvZB"));
        assertThat(response.getConnectionId(), is("0fa8593e-448e-4c98-9b21-2d95dce3adcc"));
        assertThat(response.getKeepAliveTimeout(), is(80000L));
        assertThat(response.getDisconnectTimeout(), is(120000L));
        assertThat(response.getTryWebSockets(), is(true));
        assertThat(response.getProtocolVersion(), is("1.3"));
        assertThat(response.getTransportConnectTimeout(), is(5.0));
    }

    @Test
    public void deserializeWithoutKeepAliveTimeoutTest() {
        // Arrange
        final String data = "{\"Url\":\"/signalr\",\"ConnectionToken\":\"Z1IuK7USZw4BwrDFbF8S+Hec4Mitkwe0+3N/FEZoQD8yVObSvtFdUfUJhKprVhjfXYlu1OLv2em/zMGN5ZK0Vr5H6ZqTvXaH+7Y8ee7yGjATfvZB\",\"ConnectionId\":\"0fa8593e-448e-4c98-9b21-2d95dce3adcc\",\"DisconnectTimeout\":120.0,\"TryWebSockets\":true,\"ProtocolVersion\":\"1.3\",\"TransportConnectTimeout\":5.0}";

        // Act
        final NegotiationResponse response = _serializer.fromJson(data, NegotiationResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getUrl(), is("/signalr"));
        assertThat(response.getConnectionToken(), is("Z1IuK7USZw4BwrDFbF8S+Hec4Mitkwe0+3N/FEZoQD8yVObSvtFdUfUJhKprVhjfXYlu1OLv2em/zMGN5ZK0Vr5H6ZqTvXaH+7Y8ee7yGjATfvZB"));
        assertThat(response.getConnectionId(), is("0fa8593e-448e-4c98-9b21-2d95dce3adcc"));
        assertThat(response.getKeepAliveTimeout(), is(-1L));
        assertThat(response.getDisconnectTimeout(), is(120000L));
        assertThat(response.getTryWebSockets(), is(true));
        assertThat(response.getProtocolVersion(), is("1.3"));
        assertThat(response.getTransportConnectTimeout(), is(5.0));
    }
}
