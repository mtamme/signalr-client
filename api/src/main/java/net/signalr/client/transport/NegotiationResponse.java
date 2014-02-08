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

import net.signalr.client.json.JsonName;
import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonReader;

/**
 * Represents a negotiation response.
 */
public final class NegotiationResponse implements JsonReadable {

    @JsonName("Url")
    private String _url;

    @JsonName("ConnectionToken")
    private String _connectionToken;

    @JsonName("ConnectionId")
    private String _connectionId;

    @JsonName("ProtocolVersion")
    private String _protocolVersion;

    @JsonName("TryWebSockets")
    private boolean _tryWebSockets;

    /**
     * The keep-alive timeout in seconds.
     */
    @JsonName("KeepAliveTimeout")
    private Double _keepAliveTimeout;

    /**
     * The disconnect timeout in seconds.
     */
    @JsonName("DisconnectTimeout")
    private double _disconnectTimeout;

    /**
     * The transport connect timeout in seconds.
     */
    @JsonName("TransportConnectTimeout")
    private double _transportConnectTimeout;

    public String getUrl() {
        return _url;
    }

    public String getConnectionToken() {
        return _connectionToken;
    }

    public String getConnectionId() {
        return _connectionId;
    }

    public String getProtocolVersion() {
        return _protocolVersion;
    }

    public boolean getTryWebSockets() {
        return _tryWebSockets;
    }

    public long getDisconnectTimeout() {
        return (long) (_disconnectTimeout * 1000L);
    }

    public long getKeepAliveTimeout() {
        return (_keepAliveTimeout != null) ? (long) (_keepAliveTimeout * 1000L) : -1L;
    }

    public double getTransportConnectTimeout() {
        return _transportConnectTimeout;
    }

    @Override
    public void readJson(JsonReader reader) {
        _url = reader.getString("Url");
        _connectionToken = reader.getString("ConnectionToken");
        _connectionId = reader.getString("ConnectionId");
        _protocolVersion = reader.getString("ProtocolVersion");
        _tryWebSockets = reader.getBoolean("TryWebSockets");
        _keepAliveTimeout = reader.getDouble("KeepAliveTimeout");
        _disconnectTimeout = reader.getDouble("DisconnectTimeout");
        _transportConnectTimeout = reader.getDouble("TransportConnectTimeout");
    }
}
