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

import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonReader;

/**
 * Represents a negotiation response.
 */
public final class NegotiationResponse implements JsonReadable {

    private String _url;

    private String _connectionToken;

    private String _connectionId;

    private String _protocolVersion;

    private boolean _tryWebSockets;

    /**
     * The keep-alive timeout in seconds.
     */
    private Double _keepAliveTimeout;

    /**
     * The disconnect timeout in seconds.
     */
    private Double _disconnectTimeout;

    /**
     * The transport connect timeout in seconds.
     */
    private Double _transportConnectTimeout;

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
    public void readJson(final JsonReader reader) {
        reader.readBeginObject();

        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("Url")) {
                _url = reader.readString();
            } else if (name.equalsIgnoreCase("ConnectionToken")) {
                _connectionToken = reader.readString();
            } else if (name.equalsIgnoreCase("ConnectionId")) {
                _connectionId = reader.readString();
            } else if (name.equalsIgnoreCase("ProtocolVersion")) {
                _protocolVersion = reader.readString();
            } else if (name.equalsIgnoreCase("TryWebSockets")) {
                _tryWebSockets = reader.readBoolean();
            } else if (name.equalsIgnoreCase("KeepAliveTimeout")) {
                _keepAliveTimeout = reader.readDouble();
            } else if (name.equalsIgnoreCase("DisconnectTimeout")) {
                _disconnectTimeout = reader.readDouble();
            } else if (name.equalsIgnoreCase("TransportConnectTimeout")) {
                _transportConnectTimeout = reader.readDouble();
            }
        }

        reader.readEndObject();

    }
}
